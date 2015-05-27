<?php

namespace App\Middleware;

use Jwt\Authentication\JWT;
use Slim\Middleware;
use stdClass;

class AuthenticationMiddleware extends Middleware
{
    const UNAUTHORIZED_MESSAGE = 'Unauthorized';
    const FORBIDDEN_MESSAGE = 'Forbidden';

    private $authenticationKey;

    private $authenticationHeader;

    private $authenticationRouting;

    function __construct($authenticationKey,
                         $authenticationHeader,
                         array $authenticationRouting)
    {
        $this->authenticationKey = $authenticationKey;
        $this->authenticationHeader = $authenticationHeader;
        $this->authenticationRouting = $authenticationRouting;
    }

    public function call()
    {
        if ($this->isSecuredEndpoint()) {
            $this->securedCall();
        } else {
            $this->getNextMiddleware()->call();
        }
    }

    private function isSecuredEndpoint()
    {
        return isset($this->authenticationRouting[$this->getRequestResource()]);
    }

    private function securedCall()
    {
        $jwtToken = $this->getApplication()->request()->headers($this->authenticationHeader);
        if ($jwtToken != null) {
            $loggedUser = JWT::decode($jwtToken, $this->authenticationKey);
            if ($this->isUnauthorized($loggedUser)) {
                $this->getApplication()->response->setStatus(403);
                $this->getApplication()->response()->setBody(self::UNAUTHORIZED_MESSAGE);
            } else {
                $this->getNextMiddleware()->call();
            }
        } else {
            $this->getApplication()->response->setStatus(401);
            $this->getApplication()->response()->setBody(self::FORBIDDEN_MESSAGE);
        }
    }

    private function getRequestResource()
    {
        return $this->getApplication()->request()->getResourceUri();
    }

    private function isUnauthorized(stdClass $loggedUser)
    {
        return $loggedUser == null ||
        !is_array($loggedUser->privileges) ||
        !in_array($this->getRequiredRole(), $loggedUser->privileges);
    }

    private function getRequiredRole()
    {
        return $this->authenticationRouting[$this->getRequestResource()];
    }
}