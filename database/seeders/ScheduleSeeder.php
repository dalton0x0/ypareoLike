<?php

namespace Database\Seeders;

use App\Models\Lesson;
use App\Models\Schedule;
use App\Models\Training;
use App\Models\User;
use Carbon\Carbon;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class ScheduleSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $trainings = Training::all();
        $teachers = User::whereHas('roles', fn($q) => $q->where('name', 'teacher'))->get();
        $lessons = Lesson::all();

        foreach ($trainings as $training) {

            for ($day = 1; $day <= 5; $day++) {
                $hour = 9;
                $maxLessons = 9;

                for ($i = 0; $i < $maxLessons; $i++) {
                    Schedule::create([
                        'lesson_id' => $lessons->random()->id,
                        'training_id' => $training->id,
                        'user_id' => $teachers->random()->id,
                        'day_of_week' => $day,
                        'start_hour' => sprintf('%02d:00', $hour),
                        'end_hour' => sprintf('%02d:00', $hour + 1),
                        'room' => 'Salle '.rand(1, 20),
                    ]);

                    $hour += 1;
                    if ($hour == 13) $hour = 14;
                }
            }
        }
    }
}
