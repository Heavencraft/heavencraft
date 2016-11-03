package fr.heavencraft.heavenproxy.database.banlist;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.async.AbstractQuery;
import fr.heavencraft.heavenproxy.database.users.User;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public class BanQuery extends AbstractQuery
{
    private static final String QUERY = "REPLACE INTO banlist (uuid, name, banned_by, reason) VALUES (?, ?, ?, ?);";

    private final User user;
    private final String bannedBy;
    private final String reason;

    public BanQuery(User user, String bannedBy, String reason)
    {
        this.user = user;
        this.bannedBy = bannedBy;
        this.reason = reason;
    }

    @Override
    public void executeQuery() throws HeavenException, SQLException
    {
        try (PreparedStatement ps = HeavenProxy.getConnection().prepareStatement(QUERY))
        {
            ps.setString(1, user.getUniqueIdAsString());
            ps.setString(2, user.getName().toLowerCase());
            ps.setString(3, bannedBy);
            ps.setString(4, reason);

            System.out.println("Executing query " + ps);
            ps.executeUpdate();
        }
    }
}