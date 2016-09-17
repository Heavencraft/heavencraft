package fr.hc.rp.db.towns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class AddMayorQuery implements Query
{
	private static final String QUERY = "INSERT INTO towns_users (town_id, user_id) VALUES (?, ?);";

	private final Town town;
	private final User user;
	private final TownProvider provider;

	public AddMayorQuery(Town town, User user, TownProvider provider)
	{
		this.town = town;
		this.user = user;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (town.isMayor(user))
			return; // Nothing to do

		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, town.getId());
			ps.setInt(2, user.getId());

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() != 1)
				throw new DatabaseErrorException();

			provider.invalidateCache(town);
		}
	}
}