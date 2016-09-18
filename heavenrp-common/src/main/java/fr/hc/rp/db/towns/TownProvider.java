package fr.hc.rp.db.towns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.exceptions.TownNotFoundException;

public class TownProvider
{
	private static final String SELECT_TOWN_BY_NAME = "SELECT * FROM towns WHERE name = ? LIMIT 1;";
	private static final String SELECT_MAYORS_BY_TOWN_ID = "SELECT user_id FROM towns_users WHERE town_id = ?;";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ConnectionProvider connectionProvider;
	private final TownCache cache = new TownCache();

	public TownProvider(ConnectionProvider connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public Town getTownByName(String name) throws HeavenException
	{
		// Try to get bank account from cache
		Town town = cache.getTownByName(name);
		if (town != null)
			return town;

		// Get user from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_TOWN_BY_NAME))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new TownNotFoundException(name);

			town = new Town(rs);

			try (PreparedStatement ps2 = connection.prepareStatement(SELECT_MAYORS_BY_TOWN_ID))
			{
				ps2.setInt(1, town.getId());

				final ResultSet rs2 = ps2.executeQuery();
				while (rs2.next())
					town.addMayor(rs2.getInt("user_id"));
			}

			cache.addToCache(town);
			return town;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_TOWN_BY_NAME, ex);
			throw new DatabaseErrorException();
		}
	}

	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	public void invalidateCache(Town town)
	{
		cache.invalidateCache(town);
	}
}