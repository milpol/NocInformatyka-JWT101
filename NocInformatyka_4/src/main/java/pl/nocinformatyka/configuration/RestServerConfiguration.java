package pl.nocinformatyka.configuration;

import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class RestServerConfiguration extends Configuration
{
    @NotNull
    private String secret;

    public String getSecret()
    {
        return secret;
    }

    public void setSecret(String secret)
    {
        this.secret = secret;
    }
}