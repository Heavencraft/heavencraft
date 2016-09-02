package fr.hc.core.db.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.HeavenCoreInstance;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class UpdateUserNameQuery implements Query
{
	private static final String QUERY = "UPDATE users SET name = ? WHERE id = ? LIMIT 1;";

	private final User user;
	private final String name;
	private final UserProvider<? extends User> userProvider;

	public UpdateUserNameQuery(User user, String name, UserProvider<? extends User> userProvider)
	{
		this.user = user;
		this.name = name;
		this.userProvider = userProvider;

		HeavenCoreInstance.get().getTaskManager().schedule(this);
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (name.equals(user.getName()))
			return; // Nothing to do

		try (Connection connection = userProvider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setString(1, name);
			ps.setInt(2, user.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			userProvider.invalidateCache(user);
		}
	}
}