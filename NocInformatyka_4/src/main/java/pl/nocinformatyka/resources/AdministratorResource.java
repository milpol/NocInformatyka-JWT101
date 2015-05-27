package pl.nocinformatyka.resources;

import pl.nocinformatyka.auth.annotation.RestrictedTo;
import pl.nocinformatyka.beans.Privilege;
import pl.nocinformatyka.beans.User;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/administration")
public class AdministratorResource
{
    @POST
    public String getContent(@RestrictedTo({Privilege.USER, Privilege.ADMINISTRATOR}) User user)
    {
        return "Administrator resource";
    }
}