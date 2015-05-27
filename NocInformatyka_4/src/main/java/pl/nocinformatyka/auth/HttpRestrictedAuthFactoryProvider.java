package pl.nocinformatyka.auth;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;
import pl.nocinformatyka.auth.annotation.RestrictedTo;
import pl.nocinformatyka.beans.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HttpRestrictedAuthFactoryProvider extends AbstractValueFactoryProvider
{
    private final HttpRestrictedToAuthFactory httpRestrictedToAuthFactory;

    @Inject
    public HttpRestrictedAuthFactoryProvider(MultivaluedParameterExtractorProvider extractorProvider,
                                             ServiceLocator serviceLocator,
                                             HttpRestrictedToAuthFactory httpRestrictedToAuthFactory)
    {
        super(extractorProvider, serviceLocator, Parameter.Source.UNKNOWN);
        this.httpRestrictedToAuthFactory = httpRestrictedToAuthFactory;
    }

    @Override
    protected Factory<?> createValueFactory(Parameter parameter)
    {
        final Class<?> rawType = parameter.getRawType();
        final RestrictedTo restrictedTo = parameter.getAnnotation(RestrictedTo.class);

        if (restrictedTo != null && rawType.isAssignableFrom(User.class)) {
            return httpRestrictedToAuthFactory.byPrivileges(restrictedTo.value());
        } else {
            return null;
        }
    }

    public static class AuthInjectorResolver extends ParamInjectionResolver<RestrictedTo>
    {
        public AuthInjectorResolver()
        {
            super(HttpRestrictedAuthFactoryProvider.class);
        }
    }

    public static class Binder extends AbstractBinder
    {
        private final HttpRestrictedToAuthFactory httpRestrictedToAuthFactory;

        public Binder(HttpRestrictedToAuthFactory httpRestrictedToAuthFactory)
        {
            this.httpRestrictedToAuthFactory = httpRestrictedToAuthFactory;
        }

        @Override
        protected void configure()
        {
            bind(httpRestrictedToAuthFactory).to(HttpRestrictedToAuthFactory.class);
            bind(HttpRestrictedAuthFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
            bind(AuthInjectorResolver.class).to(
                    new TypeLiteral<InjectionResolver<RestrictedTo>>()
                    {
                    }
            ).in(Singleton.class);
        }
    }
}
