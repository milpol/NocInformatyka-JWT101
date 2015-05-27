package pl.nocinformatyka.auth;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import jersey.repackaged.com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import pl.nocinformatyka.beans.Privilege;
import pl.nocinformatyka.beans.User;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Set;

public class HttpRestrictedToAuthFactory extends AbstractContainerRequestValueFactory<User>
{
    private static final String AUTH_HEADER = "X-Authorization";

    private final JwtAuthenticator jwtAuthenticator;

    private final Set<Privilege> restrictedPrivileges;

    public HttpRestrictedToAuthFactory(final JwtAuthenticator jwtAuthenticator,
                                       final Set<Privilege> restrictedPrivileges)
    {
        this.jwtAuthenticator = jwtAuthenticator;
        this.restrictedPrivileges = restrictedPrivileges;
    }

    @Override
    public User provide()
    {
        final String authHeader = getContainerRequest().getHeaderString(AUTH_HEADER);
        if (StringUtils.isEmpty(authHeader)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return getUser(authHeader);
    }

    private User getUser(String authHeader)
    {
        try {
            final Optional<User> user = jwtAuthenticator.authenticate(authHeader);
            if (user.isPresent()) {
                final Set<Privilege> userPrivileges = user.get().getPrivileges();
                if (CollectionUtils.isNotEmpty(userPrivileges) && userPrivileges.containsAll(restrictedPrivileges)) {
                    return user.get();
                } else {
                    throw new WebApplicationException(Response.Status.FORBIDDEN);
                }
            } else {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        } catch (AuthenticationException e) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Factory<?> byPrivileges(Privilege[] privileges)
    {
        return new HttpRestrictedToAuthFactory(jwtAuthenticator, Sets.newHashSet(privileges));
    }

    public static Binder binder(HttpRestrictedToAuthFactory httpRestrictedToAuthFactory)
    {
        return new HttpRestrictedAuthFactoryProvider.Binder(httpRestrictedToAuthFactory);
    }
}