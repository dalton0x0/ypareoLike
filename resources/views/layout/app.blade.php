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

<main class="d-flex justify-content-center align-items-center mt-3">
    <h1>Ypareo Like</h1>
    @yield('content')
</main>

<script src="{{ mix('js/app.js') }}"></script>
</body>
</html>
