package fr.heavencraft.heavenproxy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.heavenproxy.async.AbstractQuery;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public class UpdateUUIDQuery extends AbstractQuery
{
    private static final String USERS_UPDATE_QUERY = "UPDATE users SET uuid = ? WHERE uuid = ?;";

    private final String uuid;

    public UpdateUUIDQuery(String uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public void executeQuery() throws HeavenException, SQLException
    {
        final String dash = uuid.substring(0, 8) //
                + "-" + uuid.substring(8, 12) //
                + "-" + uuid.substring(12, 16) //
                + "-" + uuid.substring(16, 20) //
                + "-" + uuid.substring(20, 32);

        try (PreparedStatement ps = HeavenProxy.getConnection().prepareStatement(USERS_UPDATE_QUERY))
        {
            ps.setString(1, dash);
            ps.setString(2, uuid);

            if (ps.executeUpdate() == 0)
                throw new RuntimeException("Failed to update " + uuid);
        }
    }

}
