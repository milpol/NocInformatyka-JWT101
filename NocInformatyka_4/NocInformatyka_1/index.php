<?php

// credentials
const LOGIN = 'login';
const PASSWORD = 'password';
// session
const LOGGED = 'logged';

session_start();

if (!empty($_POST)) {
    if ($_POST[LOGIN] === LOGIN && $_POST[PASSWORD] === PASSWORD) {
        $_SESSION[LOGGED] = true;
        redirect();
    }
}

if (isset($_GET['logout'])) {
    logout();
    redirect();
}

function isLogged()
{
    return isset($_SESSION[LOGGED]) && $_SESSION[LOGGED];
}

function logout()
{
    session_destroy();
}

function redirect()
{
    return header('Location: index.php');
}

?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Back in ol' 2000</title>
</head>
<body>
<?php if (isLogged()): ?>
    <p>Look ma' me logged!</p>

    <p><a href="?logout">Now exit</a></p>
<?php else: ?>
    <form method="post" action="">
        <fieldset>
            <legend>Log in</legend>
            <label>Login:
                <input type="text" name="login">
            </label>
            <label>Password:
                <input type="password" name="password">
            </label>
            <input type="submit" value="Log me in">
        </fieldset>
    </form>
<?php endif; ?>

</body>
</html>