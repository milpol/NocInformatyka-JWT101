package pl.nocinformatyka.resources;

import pl.nocinformatyka.auth.annotation.RestrictedTo;
import pl.nocinformatyka.beans.Privilege;
import pl.nocinformatyka.beans.User;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/moderation")
public class ModeratorResource
{
    @POST
    public String getContent(@RestrictedTo({Privilege.USER, Privilege.MODERATOR}) User user)
    {
        return "Moderator resource";
    }
}