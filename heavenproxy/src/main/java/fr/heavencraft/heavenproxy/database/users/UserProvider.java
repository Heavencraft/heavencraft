package fr.heavencraft.heavenproxy.database.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.exceptions.SQLErrorException;
import fr.heavencraft.heavenproxy.exceptions.UserNotFoundException;

public class UserProvider
{
    private static final String SELECT_USER_BY_UNIQUE_ID = "SELECT * FROM users WHERE uuid = ? LIMIT 1;";
    private static final String SELECT_USER_BY_NAME = "SELECT * FROM users WHERE name = ? LIMIT 1;";
    private static final String INSERT_USER = "INSERT INTO users (uuid, name) VALUES (?, ?)";

    public static User getUserByUniqueId(UUID uniqueId) throws UserNotFoundException, SQLErrorException
    {
        // Try to get user from cache
        User user = UserCache.getUserByUniqueId(uniqueId);
        if (user != null)
            return user;

        // Get user from database
        try (PreparedStatement ps = HeavenProxy.getConnection().prepareStatement(SELECT_USER_BY_UNIQUE_ID))
        {
            ps.setString(1, uniqueId.toString());

            final ResultSet rs = ps.executeQuery();

            if (!rs.next())
                throw new UserNotFoundException(uniqueId);

            user = new User(rs);
            UserCache.addToCache(user);
            return user;
        }
        catch (final SQLException ex)
        {
            ex.printStackTrace();
            throw new SQLErrorException();
        }
    }

    public static User getUserByName(String name) throws UserNotFoundException, SQLErrorException
    {
        // Try to get user from cache
        User user = UserCache.getUserByName(name);
        if (user != null)
            return user;

        // Get user from database
        try (PreparedStatement ps = HeavenProxy.getConnection().prepareStatement(SELECT_USER_BY_NAME))
        {
            ps.setString(1, name);

            final ResultSet rs = ps.executeQuery();

            if (!rs.next())
                throw new UserNotFoundException(name);

            user = new User(rs);
            UserCache.addToCache(user);
            return user;
        }
        catch (final SQLException ex)
        {
            ex.printStackTrace();
            throw new SQLErrorException();
        }
    }

    public static void createUser(UUID uniqueId, String name) throws SQLErrorException
    {
        try (PreparedStatement ps = HeavenProxy.getConnection().prepareStatement(INSERT_USER))
        {
            ps.setString(1, uniqueId.toString());
            ps.setString(2, name);

            ps.executeUpdate();
        }
        catch (final SQLException ex)
        {
            ex.printStackTrace();
            throw new SQLErrorException();
        }
    }
}