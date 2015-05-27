<?php

namespace App\Service;

use App\Repository\UserRepository;
use InvalidArgumentException;
use Jwt\Authentication\JWT;
use stdClass;

class AuthenticationService
{
    private $userRepository;

    private $authenticationKey;

    function __construct(UserRepository $userRepository, $authenticationKey)
    {
        $this->userRepository = $userRepository;
        $this->authenticationKey = $authenticationKey;
    }

    public function login(stdClass $user)
    {
        $this->validateUser($user);
        $loggedUser = $this->userRepository->getByUsernameAndPassword($user->username, $user->password);
        return array(
            'user' => $loggedUser,
            'token' => JWT::encode($loggedUser, $this->authenticationKey));
    }

    public function validateUser(stdClass $user)
    {
        if ($user == null || !isset($user->username) || !isset($user->password)) {
            throw new InvalidArgumentException('No user provided');
        }
    }
}