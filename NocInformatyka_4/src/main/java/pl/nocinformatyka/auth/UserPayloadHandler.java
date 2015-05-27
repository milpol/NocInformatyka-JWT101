package pl.nocinformatyka.auth;

import com.auth0.jwt.PayloadHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.nocinformatyka.beans.User;

public class UserPayloadHandler implements PayloadHandler
{
    private final ObjectMapper objectMapper;

    public UserPayloadHandler(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Override
    public String encoding(Object payload) throws Exception
    {
        return objectMapper.writeValueAsString(payload);
    }

    @Override
    public Object decoding(String payload) throws Exception
    {
        return objectMapper.readValue(payload, User.class);
    }
}