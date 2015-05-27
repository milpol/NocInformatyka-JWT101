<?php

namespace App\Repository;

use App\Role;
use InvalidArgumentException;

class UserRepository
{
    public function getByUsernameAndPassword($username, $password)
    {
        $user = null;
        if ($username === 'user' && $password === '123') {
            $user = array(
                'username' => 'User',
                'privileges' => array(Role::User)
            );
        } else if ($username === 'moderator' && $password === '1234') {
            $user = array(
                'username' => 'Moderator',
                'privileges' => array(Role::User, Role::Moderator)
            );
        } else if ($username === 'administrator' && $password === '12345') {
            $user = array(
                'username' => 'Administrator',
                'privileges' => array(Role::User, Role::Moderator, Role::Administrator)
            );
        } else {
            throw new InvalidArgumentException('User ' . $username . ' not found');
        }
        return $user;
    }
}