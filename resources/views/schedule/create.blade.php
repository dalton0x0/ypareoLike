@extends('layout.app')

@section('content')
    <div class="container">
        <h1>Ajouter un cours au planning</h1>

        <form method="POST" action="{{ route('schedule.store') }}">
            @csrf

            <div class="form-group">
                <label>Cours</label>
                <select name="lesson_id" class="form-control" required>
                    @foreach($lessons as $lesson)
                        <option value="{{ $lesson->id }}">{{ $lesson->label }}</option>
                    @endforeach
                </select>
            </div>

            <div class="form-group">
                <label>Formation</label>
                <select name="training_id" class="form-control" required>
                    @foreach($trainings as $training)
                        <option value="{{ $training->id }}">
                            {{ $training->title }} (Niveau {{ $training->level }})
                        </option>
                    @endforeach
                </select>
            </div>

            <div class="form-group">
                <label>Professeur</label>
                <select name="user_id" class="form-control" required>
                    @foreach($teachers as $teacher)
                        <option value="{{ $teacher->id }}">
                            {{ $teacher->firstName }} {{ $teacher->lastName }}
                        </option>
                    @endforeach
                </select>
            </div>

            <div class="form-group">
                <label>Jour</label>
                <select name="day_of_week" class="form-control" required>
                    @foreach($days as $key => $day)
                        <option value="{{ $key }}">{{ $day }}</option>
                    @endforeach
                </select>
            </div>

            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Heure de début</label>
                    <select name="start_hour" class="form-control" required>
                        @foreach($hours as $hour)
                            <option value="{{ $hour }}">{{ $hour }}</option>
                        @endforeach
                    </select>
                </div>
                <div class="form-group col-md-6">
                    <label>Heure de fin</label>
                    <select name="end_hour" class="form-control" required>
                        @foreach($hours as $hour)
                            <option value="{{ $hour }}"
                                {{ $loop->last ? 'selected' : '' }}>
                                {{ $hour }}
                            </option>
                        @endforeach
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label>Salle</label>
                <input type="text" name="room" class="form-control">
            </div>

            <button type="submit" class="btn btn-primary">Ajouter au planning</button>
        </form>
    </div>
@endsection
