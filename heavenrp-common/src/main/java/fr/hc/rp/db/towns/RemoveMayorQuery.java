package fr.hc.rp.db.towns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class RemoveMayorQuery implements Query
{
	private static final String QUERY = "DELETE FROM towns_users WHERE town_id = ? AND user_id = ? LIMIT 1;";

	private final Town town;
	private final User user;
	private final TownProvider provider;

	public RemoveMayorQuery(Town town, User user, TownProvider provider)
	{
		this.town = town;
		this.user = user;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (!town.isMayor(user))
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