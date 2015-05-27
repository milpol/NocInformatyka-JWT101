<?php

use App\Configuration;
use App\Middleware\AuthenticationMiddleware;
use App\Repository\UserRepository;
use App\Service\AuthenticationService;
use Slim\Slim;

require '../src/Slim/Slim.php';

Slim::registerAutoloader();

$userRepository = new UserRepository();

$application = new Slim(array('debug' => false));

$application->add(new AuthenticationMiddleware(
    Configuration::get('authenticationKey'),
    Configuration::get('authenticationHeader'),
    Configuration::get('routes')
));

$application->container->singleton('authenticationService', function () use ($userRepository) {
    return new AuthenticationService($userRepository, Configuration::get('authenticationKey'));
});

$application->group('/auth', function () use ($application) {
    $application->post('/login', function () use ($application) {
        try {
            $request = $application->request();
            echo json_encode($application->authenticationService->login(json_decode($request->getBody())));
        } catch (Exception $e) {
            $application->halt(403, $e->getMessage());
        }
    });
});

$application->post('/account', function () {
    echo json_encode(array('info' => 'account'));
});

$application->post('/moderation', function () {
    echo json_encode(array('info' => 'moderation'));
});

$application->post('/administration', function () {
    echo json_encode(array('info' => 'administration'));
});

$application->response()->header('Content-Type', 'application/json');
$application->run();