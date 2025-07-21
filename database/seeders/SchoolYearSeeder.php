<?php

namespace Database\Seeders;

use App\Models\SchoolYear;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class SchoolYearSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        SchoolYear::factory()->createMany([
            ['label' => '2022 - 2023', 'start_year' => '2022', 'end_year' => '2023'],
            ['label' => '2024 - 2025', 'start_year' => '2025', 'end_year' => '2025'],
        ]);
    }
}
