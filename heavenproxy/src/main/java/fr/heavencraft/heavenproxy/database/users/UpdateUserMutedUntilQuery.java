package fr.heavencraft.heavenproxy.database.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.HeavenProxyInstance;
import fr.heavencraft.heavenproxy.async.AbstractQuery;

public class UpdateUserMutedUntilQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET muted_until = DATE_ADD(NOW(), INTERVAL ? MINUTE) WHERE uuid = ? LIMIT 1;";

	private final User user;
	private final int nbMinutes;

	public UpdateUserMutedUntilQuery(User user, int nbMinutes)
	{
		this.user = user;
		this.nbMinutes = nbMinutes;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = HeavenProxyInstance.get().getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, nbMinutes);
			ps.setString(2, user.getUniqueIdAsString());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			UserCache.invalidateCache(user);
		}
	}
}