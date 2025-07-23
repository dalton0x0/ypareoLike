<!doctype html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <title>@yield('title', config('app.name')) | {{ config('app.name') }}</title>

    <link rel="stylesheet" href="{{ mix('css/app.css') }}">
    <link rel="stylesheet" href="{{ mix('css/styles.css') }}">
</head>
<body class="d-flex flex-column h-100 bg-light">

<main class="container flex-column justify-content-center align-items-center mt-3">
    <h1 class="text-center mb-4">Ypareo Like</h1>
    <section>
        <h1 class="text-center mb-4">All users</h1>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Full name</th>
                <th scope="col">Status</th>
                <th scope="col">Formation</th>
                <th scope="col">School year</th>
            </tr>
            </thead>
            <tbody>
            @foreach($users as $user)
                <tr>
                    <th scope="row">{{ $user->id }}</th>
                    <td>{{ $user->firstName }} {{ $user->lastName }}</td>
                    <td>
                        {{ $user->roles->pluck('name')->implode(', ') }}
                    </td>
                    <td>
                        {{ $user->trainings->pluck('title')->implode(', ') }}
                    </td>
                    <td>
                        @if($user->roles->contains('name', 'student'))
                            {{ $user->schoolYears->pluck('label')->implode(', ') }}
                        @endif
                    </td>
                </tr>
            @endforeach
            </tbody>
        </table>
    </section>
</main>

<script src="{{ mix('js/app.js') }}"></script>
</body>
</html>
