package fr.heavencraft.heavenproxy.database.banlist;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.async.AbstractQuery;
import fr.heavencraft.heavenproxy.database.users.User;
import fr.hc.core.exceptions.HeavenException;

public class UnbanQuery extends AbstractQuery
{
    private static final String QUERY = "DELETE FROM banlist WHERE uuid = ?;";

    private final User user;

    public UnbanQuery(User user)
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
        }
    }
}