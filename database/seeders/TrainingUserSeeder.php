<?php

namespace Database\Seeders;

use App\Models\SchoolYear;
use App\Models\Training;
use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class TrainingUserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $currentSchoolYear = SchoolYear::latest()->first();
        $students = User::whereHas('roles', fn($q) => $q->where('name', 'student'))->get();
        $trainings = Training::all();

        DB::table('training_user')->truncate();

        foreach ($students as $student) {
            $studentTrainings = $trainings->random(rand(1, 2));

            foreach ($studentTrainings as $training) {
                $exists = DB::table('training_user')
                    ->where('user_id', $student->id)
                    ->where('training_id', $training->id)
                    ->where('school_year_id', $currentSchoolYear->id)
                    ->exists();

                if (!$exists) {
                    $student->trainings()->attach($training, [
                        'school_year_id' => $currentSchoolYear->id
                    ]);
                }
            }
        }

        $teachers = User::whereHas('roles', fn($q) => $q->where('name', 'teacher'))->get();

        foreach ($teachers as $teacher) {
            $teacherTrainings = $trainings->random(rand(1, 3));

            foreach ($teacherTrainings as $training) {
                $exists = DB::table('training_user')
                    ->where('user_id', $teacher->id)
                    ->where('training_id', $training->id)
                    ->where('school_year_id', $currentSchoolYear->id)
                    ->exists();

                if (!$exists) {
                    $teacher->trainings()->attach($training, [
                        'school_year_id' => $currentSchoolYear->id
                    ]);
                }
            }
        }
    }
}
