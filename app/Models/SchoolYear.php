<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class SchoolYear extends Model
{
    /** @use HasFactory<\Database\Factories\RoleFactory> */
    use HasFactory;

    protected $fillable = [
        'label',
        'start_year',
        'end_year',
    ];

    public function users() : BelongsToMany
    {
        return $this->belongsToMany(User::class, 'training_user')
            ->withPivot('training_id')
            ->withTimestamps();
    }
}
