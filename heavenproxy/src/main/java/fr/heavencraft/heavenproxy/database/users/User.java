package fr.heavencraft.heavenproxy.database.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class User
{
    // For optimization purpose, avoid a useless UUID.toString
    private final String uniqueIdAsString;

    private final UUID uniqueId;
    private final String name;
    private final String color;
    private final Timestamp lastLogin;
    private final Timestamp mutedUntil;

    User(ResultSet rs) throws SQLException
    {
        uniqueIdAsString = rs.getString("uuid");

        uniqueId = UUID.fromString(uniqueIdAsString);
        name = rs.getString("name");
        color = rs.getString("color");
        lastLogin = rs.getTimestamp("last_login");
        mutedUntil = rs.getTimestamp("muted_until");
    }

    UUID getUniqueId()
    {
        return uniqueId;
    }

    public String getUniqueIdAsString()
    {
        return uniqueIdAsString;
    }

    public String getName()
    {
        return name;
    }

    public String getColor()
    {
        return "§" + color;
    }

    public Timestamp getLastLogin()
    {
        return lastLogin;
    }

    public boolean isMuted()
    {
        return mutedUntil != null ? mutedUntil.after(new Date()) : false;
    }
}