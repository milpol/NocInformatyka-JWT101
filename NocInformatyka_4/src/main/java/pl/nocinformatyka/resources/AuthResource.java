package pl.nocinformatyka.resources;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.ClaimSet;
import com.auth0.jwt.JwtProxy;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import pl.nocinformatyka.beans.Privilege;
import pl.nocinformatyka.beans.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource
{
    private final JwtProxy jwtProxy;

    private final String secret;

    public AuthResource(final JwtProxy jwtProxy,
                        final String secret)
    {
        this.jwtProxy = jwtProxy;
        this.secret = secret;
    }

    @POST
    @Path("/login")
    public String login(final User user)
    {
        Optional<User> loggedUser = Optional.absent();
        if (user.getUsername().equals("user") && user.getPassword().equals("123")) {
            loggedUser = Optional.of(new User(user.getUsername(), Sets.newHashSet(Privilege.USER)));
        } else if (user.getUsername().equals("moderator") && user.getPassword().equals("1234")) {
            loggedUser = Optional.of(new User(user.getUsername(), Sets.newHashSet(Privilege.USER, Privilege.MODERATOR)));
        } else if (user.getUsername().equals("administrator") && user.getPassword().equals("12345")) {
            loggedUser = Optional.of(new User(user.getUsername(), Sets.newHashSet(Privilege.values())));
        }
        if (loggedUser.isPresent()) {
            try {
                return jwtProxy.encode(Algorithm.HS512, loggedUser.get(), secret, new ClaimSet());
            } catch (Exception e) {
                throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}