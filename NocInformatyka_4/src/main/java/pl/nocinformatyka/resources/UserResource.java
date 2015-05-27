package pl.nocinformatyka.resources;

import pl.nocinformatyka.auth.annotation.RestrictedTo;
import pl.nocinformatyka.beans.Privilege;
import pl.nocinformatyka.beans.User;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/account")
public class UserResource
{
    @POST
    public String getContent(@RestrictedTo(Privilege.USER) User user)
    {
        return "User resource";
    }
}