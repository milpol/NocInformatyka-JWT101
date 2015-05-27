package pl.nocinformatyka.beans;

import java.util.Set;

public class User
{
    private String username;

    private String password;

    private Set<Privilege> privileges;

    public User()
    {
    }

    public User(String username, Set<Privilege> privileges)
    {
        this.username = username;
        this.privileges = privileges;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Set<Privilege> getPrivileges()
    {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges)
    {
        this.privileges = privileges;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (privileges != null ? !privileges.equals(user.privileges) : user.privileges != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (privileges != null ? privileges.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "username='" + username + '\'' +
                ", password=XXXXXX" +
                ", privileges=" + privileges +
                '}';
    }
}