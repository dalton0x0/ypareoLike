<?php

namespace Database\Seeders;

use App\Models\Lesson;
use App\Models\Training;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class LessonSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        Lesson::factory()->createMany([
            ['label' => "Conception d'une infrastructure sécurisée"],
            ['label' => "Sécuriser une application"],
            ['label' => "Maquetter une application"]
        ]);
    }
}
