package fr.hc.core.db.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class UpdateUserLastLoginQuery implements Query
{
	private static final String QUERY = "UPDATE users SET last_login = ? WHERE id = ? LIMIT 1;";

	private final User user;
	private final UserProvider<? extends User> userProvider;

	public UpdateUserLastLoginQuery(User user, UserProvider<? extends User> userProvider)
	{
		this.user = user;
		this.userProvider = userProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = userProvider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setTimestamp(1, new Timestamp(new Date().getTime()));
			ps.setInt(2, user.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			userProvider.invalidateCache(user);
		}
	}
}