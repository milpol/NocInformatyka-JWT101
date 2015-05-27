<?php

namespace App;

class Configuration
{
    private static $configuration = array(
        'authenticationKey' => 'keep-super-secret',
        'authenticationHeader' => 'X-Authorization',

        'routes' => array(
            '/account' => Role::User,
            '/moderation' => Role::Moderator,
            '/administration' => Role::Administrator
        )
    );

    public static function get($key)
    {
        return self::$configuration[$key];
    }
}