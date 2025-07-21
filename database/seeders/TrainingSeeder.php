<?php

namespace Database\Seeders;

use App\Models\Training;
use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class TrainingSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $trainings = Training::factory()->createMany([
            ['title' => 'BTS SIO', 'level' => 1],
            ['title' => 'BTS SIO SISR', 'level' => 2],
            ['title' => 'BTS SIO SLAM', 'level' => 2],
            ['title' => 'Bachelor Concepteur Développeur d\'Application', 'level' => 3],
        ]);

        $students = User::whereHas('roles', function ($query) {
            $query->where('name', 'student');
        })->get();

        $schoolYearIds = [1, 2];

        foreach ($students as $student) {
            $selectedTraining = $trainings->random(rand(1, 2));
            foreach ($selectedTraining as $training) {
                $student->trainings()->attach($training->id, [
                    'school_year_id' => $schoolYearIds[array_rand($schoolYearIds)],
                ]);
            }
        }
    }
}
