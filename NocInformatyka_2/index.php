<?php

// credentials
const LOGIN = 'login';
const PASSWORD = 'password';

$users = array('login' => 'password');

$user = $_SERVER['PHP_AUTH_USER'];
$password = $_SERVER['PHP_AUTH_PW'];

if ($user != LOGIN || $password != PASSWORD ) {
    header('WWW-Authenticate: Basic realm="Noc Informatyka"');
    header('HTTP/1.0 401 Unauthorized');
    die ("Not authorized");
}

?>
<!DOCTYPE html >
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>HTTP Basic Auth</title>
</head>
<body>
<p>I R so secured!</p>
</body>
</html>