<?php

namespace App\Http\Controllers;

use App\Models\LessonTraining;
use App\Models\User;
use Illuminate\Http\Request;

class ScheduleController extends Controller
{
    public function index(Request $request)
    {
        $user = User::where('id', 9)->first();
        $trainings = $user->trainings()->get();

        if ($user->roles->contains('teacher') || $user->roles->contains('admin')) {
            $schedules = LessonTraining::with(['lesson', 'training', 'teacher'])
                ->when($request->training_id, function($query) use ($request) {
                    $query->where('training_id', $request->training_id);
                })
                ->when($request->week, function($query) use ($request) {
                    $query->whereBetween('start_time', [
                        now()->startOfWeek()->addWeeks($request->week),
                        now()->endOfWeek()->addWeeks($request->week)
                    ]);
                })
                ->orderBy('start_time')
                ->get();
        } else {
            $schedules = LessonTraining::whereIn('training_id', $user->trainings->pluck('id'))
                ->with(['lesson', 'training', 'teacher'])
                ->when($request->week, function($query) use ($request) {
                    $query->whereBetween('start_time', [
                        now()->startOfWeek()->addWeeks($request->week),
                        now()->endOfWeek()->addWeeks($request->week)
                    ]);
                })
                ->orderBy('start_time')
                ->get();
        }

        return view('schedule.index', (['schedules', 'trainings']));
    }
}
