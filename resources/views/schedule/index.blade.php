@extends('layout.app')

@section('content')
    <div class="container-fluid">
        <h1>Planning des cours</h1>

        <div class="mb-4">
            <form method="GET" class="form-inline">
                <select name="training_id" class="form-control mr-2">
                    <option value="">Toutes les formations</option>
                    @foreach($trainings as $training)
                        <option value="{{ $training->id }}"
                            {{ request('training_id') == $training->id ? 'selected' : '' }}>
                            {{ $training->title }} (Niveau {{ $training->level }})
                        </option>
                    @endforeach
                </select>
                <button type="submit" class="btn btn-primary">Filtrer</button>
            </form>
        </div>

        <div class="table-responsive">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Heure</th>
                    @foreach($weekDays as $day)
                        <th>{{ $day }}</th>
                    @endforeach
                </tr>
                </thead>
                <tbody>
                @foreach($timeSlots as $time)
                    <tr>
                        <td>{{ $time }}</td>
                        @foreach(array_keys($weekDays) as $day)
                            <td>
                                @foreach(($schedules[$day] ?? []) as $schedule)
                                    @if($schedule->start_hour <= $time && $schedule->end_hour > $time)
                                        <div class="schedule-item p-2 mb-2 bg-light rounded">
                                            <strong>{{ $schedule->lesson->label }}</strong><br>
                                            {{ $schedule->training->title }}<br>
                                            Salle: {{ $schedule->room }}<br>
                                            Prof: {{ $schedule->teacher->firstName }}
                                        </div>
                                    @endif
                                @endforeach
                            </td>
                        @endforeach
                    </tr>
                @endforeach
                </tbody>
            </table>
        </div>
    </div>
@endsection
