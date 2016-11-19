package fr.heavencraft.heavenproxy.database.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.HeavenProxyInstance;
import fr.heavencraft.heavenproxy.async.AbstractQuery;

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
		try (Connection connection = HeavenProxyInstance.get().getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setString(1, user.getUniqueIdAsString());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			UserCache.invalidateCache(user);
		}
	}
}