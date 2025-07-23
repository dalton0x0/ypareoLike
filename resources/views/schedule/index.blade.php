@extends('layout.app')

@section('content')
    <div class="container-fluid">

        <h1>Planning des cours</h1>

        <div class="table-responsive">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Date/Heure</th>
                    <th>Cours</th>
                    <th>Formation</th>
                    <th>Enseignant</th>
                    <th>Salle</th>
                </tr>
                </thead>
                <tbody>
                @foreach($schedules as $schedule)
                    <tr>
                        <td>
                            {{ $schedule->start_time->format('d/m/Y H:i') }}
                            - {{ $schedule->end_time->format('H:i') }}
                        </td>
                        <td>{{ $schedule->lesson->label }}</td>
                        <td>{{ $schedule->training->title }}</td>
                        <td>{{ $schedule->teacher->firstName }} {{ $schedule->teacher->lastName }}</td>
                        <td>{{ $schedule->room ?? '-' }}</td>
                    </tr>
                @endforeach
                </tbody>
            </table>
        </div>
    </div>
@endsection
