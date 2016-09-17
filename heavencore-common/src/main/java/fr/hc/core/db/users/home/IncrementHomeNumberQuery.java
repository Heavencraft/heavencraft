package fr.hc.core.db.users.home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.db.users.UserProvider;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class IncrementHomeNumberQuery implements Query
{
	private static final String QUERY = "UPDATE users SET home_number = home_number + 1 WHERE id = ? LIMIT 1";

	private final UserWithHome user;
	private final UserProvider<? extends UserWithHome> provider;

	public IncrementHomeNumberQuery(UserWithHome user, UserProvider<UserWithHome> provider)
	{
		this.user = user;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, user.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			provider.invalidateCache(user);
		}
	}
}