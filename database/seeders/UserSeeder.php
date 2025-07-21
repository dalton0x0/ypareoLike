<?php

namespace Database\Seeders;

use App\Models\Role;
use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class  UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $admin = User::factory()->create([
            'firstName' => 'Admin',
            'lastName' => 'ADMIN',
            'username' => 'admin',
            'email' => 'admin@example.test',
            'password' => Hash::make('password'),
        ]);
        $admin->roles()->sync([1]);

        User::factory()->count(2)->create()->each(function ($user) {
            $user->roles()->sync([1, 2]);
        });

        User::factory()->count(3)->create()->each(function ($user) {
            $user->roles()->sync([2]);
        });

        User::factory()->count(5)->create()->each(function ($user) {
            $user->roles()->sync([3]);
        });
    }
}
