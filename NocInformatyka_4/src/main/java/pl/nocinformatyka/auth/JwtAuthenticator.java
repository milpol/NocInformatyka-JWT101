package pl.nocinformatyka.auth;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JwtProxy;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import pl.nocinformatyka.beans.User;

public class JwtAuthenticator implements Authenticator<String, User>
{
    private final JwtProxy jwtProxy;

    private final String secret;

    public JwtAuthenticator(JwtProxy jwtProxy, String secret)
    {
        this.jwtProxy = jwtProxy;
        this.secret = secret;
    }

    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException
    {
        try {
            final User user = (User) jwtProxy.decode(Algorithm.HS512, token, secret);
            return Optional.fromNullable(user);
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
    }
}