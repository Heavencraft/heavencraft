package fr.heavencraft.heavenproxy.database.users;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.async.AbstractQuery;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public class UpdateUserLastLoginQuery extends AbstractQuery
{
    private static final String QUERY = "UPDATE users SET last_login = NOW() WHERE uuid = ? LIMIT 1;";

    private final User user;

    public UpdateUserLastLoginQuery(User user)
    {
        this.user = user;
    }

    @Override
    public void executeQuery() throws HeavenException, SQLException
    {
        try (PreparedStatement ps = HeavenProxy.getConnection().prepareStatement(QUERY))
        {
            ps.setString(1, user.getUniqueIdAsString());

            System.out.println("Executing query " + ps);
            ps.executeUpdate();

            UserCache.invalidateCache(user);
        }
    }
}