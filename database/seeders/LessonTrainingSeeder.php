<?php

namespace Database\Seeders;

use App\Models\Lesson;
use App\Models\LessonTraining;
use App\Models\Training;
use App\Models\User;
use Carbon\Carbon;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class LessonTrainingSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $trainings = Training::all();
        $teachers = User::whereHas('roles', fn($q) => $q->where('name', 'teacher'))->get();
        $lessons = Lesson::all();

        for ($week = 0; $week < 4; $week++) {
            foreach ($trainings as $training) {
                $trainingLessons = $lessons->random(3);
                $trainingTeachers = $teachers->random(2);

                foreach ($trainingLessons as $lesson) {
                    $day = rand(1, 5);
                    $hour = rand(8, 16);

                    $startTime = Carbon::now()
                        ->startOfWeek()
                        ->addWeeks($week)
                        ->addDays($day - 1)
                        ->setHour($hour)
                        ->setMinute(0);

                    LessonTraining::create([
                        'lesson_id' => $lesson->id,
                        'training_id' => $training->id,
                        'user_id' => $trainingTeachers->random()->id,
                        'start_time' => $startTime,
                        'end_time' => $startTime->copy()->addHours(2),
                        'room' => 'Salle ' . rand(100, 120),
                    ]);
                }
            }
        }
    }
}
