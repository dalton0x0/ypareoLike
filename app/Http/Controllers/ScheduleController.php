<?php

namespace App\Http\Controllers;

use App\Models\Lesson;
use App\Models\Schedule;
use App\Models\Training;
use App\Models\User;
use Illuminate\Http\Request;

class ScheduleController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Request $request)
    {
        //$user = auth()->user();
        $user = User::where('id', 9)->first();
        $trainings = $user->trainings;

        $schedules = Schedule::query()
            ->with(['lesson', 'training', 'teacher'])
            ->when(
                $request->training_id,
                fn($q) => $q->where('training_id', $request->training_id)
            )
            ->when(
                $user->roles->contains('student'),
                fn($q) => $q->whereIn('training_id', $trainings->pluck('id'))
            )
            ->orderBy('day_of_week')
            ->orderBy('start_hour')
            ->get()
            ->groupBy('day_of_week');

        $weekDays = Schedule::DAYS;
        $timeSlots = Schedule::HOURS;

        return view('schedule.index', compact(
            'schedules', 'trainings', 'weekDays', 'timeSlots'
        ));
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        $lessons = Lesson::all();
        $trainings = Training::all();
        $teachers = User::whereHas('roles', fn($q) => $q->where('name', 'teacher'))->get();

        return view('schedule.create', [
            'lessons' => $lessons,
            'trainings' => $trainings,
            'teachers' => $teachers,
            'days' => Schedule::DAYS,
            'hours' => Schedule::HOURS
        ]);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'lesson_id' => 'required|exists:lessons,id',
            'training_id' => 'required|exists:trainings,id',
            'user_id' => 'required|exists:users,id',
            'day_of_week' => 'required|integer|between:1,5',
            'start_hour' => 'required|date_format:H:i',
            'end_hour' => 'required|date_format:H:i|after:start_hour',
            'room' => 'nullable|string|max:50'
        ]);

        Schedule::create($validated);

        return redirect()->route('schedule.index')->with('success', 'Cours ajouté au planning');
    }

    /**
     * Display the specified resource.
     */
    public function show(Schedule $schedule)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Schedule $schedule)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Schedule $schedule)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Schedule $schedule)
    {
        //
    }
}
