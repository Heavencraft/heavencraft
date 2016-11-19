package fr.heavencraft.heavenproxy.database.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.HeavenProxyInstance;
import fr.heavencraft.heavenproxy.async.AbstractQuery;

public class UpdateUserNameQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET name = ? WHERE uuid = ? LIMIT 1;";

	private final User user;
	private final String name;

	public UpdateUserNameQuery(User user, String name)
	{
		this.user = user;
		this.name = name;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		// Nothing to do
		if (name.equals(user.getName()))
			return;

		try (Connection connection = HeavenProxyInstance.get().getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setString(1, name);
			ps.setString(2, user.getUniqueIdAsString());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			UserCache.invalidateCache(user);
		}
	}
}