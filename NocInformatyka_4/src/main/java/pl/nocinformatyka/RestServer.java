package pl.nocinformatyka;

import com.auth0.jwt.impl.JwtProxyImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import pl.nocinformatyka.auth.HttpRestrictedToAuthFactory;
import pl.nocinformatyka.auth.JwtAuthenticator;
import pl.nocinformatyka.auth.UserPayloadHandler;
import pl.nocinformatyka.beans.Privilege;
import pl.nocinformatyka.configuration.RestServerConfiguration;
import pl.nocinformatyka.resources.AdministratorResource;
import pl.nocinformatyka.resources.AuthResource;
import pl.nocinformatyka.resources.ModeratorResource;
import pl.nocinformatyka.resources.UserResource;

import java.util.Collections;

public class RestServer extends Application<RestServerConfiguration>
{
    @Override
    public void initialize(Bootstrap<RestServerConfiguration> bootstrap)
    {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }

    @Override
    public void run(RestServerConfiguration restServerConfiguration, Environment environment) throws Exception
    {
        final ObjectMapper objectMapper = Jackson.newObjectMapper();
        final UserPayloadHandler userPayloadHandler = new UserPayloadHandler(objectMapper);
        final JwtProxyImpl jwtProxy = new JwtProxyImpl();
        jwtProxy.setPayloadHandler(userPayloadHandler);

        final AuthResource authResource = new AuthResource(jwtProxy, restServerConfiguration.getSecret());
        final UserResource userResource = new UserResource();
        final ModeratorResource moderatorResource = new ModeratorResource();
        final AdministratorResource administratorResource = new AdministratorResource();

        final JerseyEnvironment jersey = environment.jersey();

        jersey.register(HttpRestrictedToAuthFactory.binder(new HttpRestrictedToAuthFactory(
                new JwtAuthenticator(jwtProxy, restServerConfiguration.getSecret()),
                Collections.<Privilege>emptySet()
        )));

        jersey.register(authResource);
        jersey.register(userResource);
        jersey.register(moderatorResource);
        jersey.register(administratorResource);
    }

    public static void main(String[] args) throws Exception
    {
        new RestServer().run(args);
    }
}