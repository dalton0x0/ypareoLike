<?php

use App\Models\User;
use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    $users = User::with('trainings', 'roles', 'schoolYears')->get();
    return view('layout.app', [
        'users' => $users
    ]);
});
