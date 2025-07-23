<?php

use App\Http\Controllers\ScheduleController;
use App\Models\User;
use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    $users = User::with('trainings', 'roles', 'schoolYears')->get();
    return view('test.test', [
        'users' => $users
    ]);
});

Route::get('/schedule', [ScheduleController::class, 'index'])->name('schedule.index');
