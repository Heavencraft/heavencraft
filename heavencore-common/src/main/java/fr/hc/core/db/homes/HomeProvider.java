package fr.hc.core.db.homes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;

public class HomeProvider
{
	private static final String SELECT_HOME_BY_USER_AND_NUMBER = "SELECT * FROM homes WHERE user_id = ? AND home_number = ? LIMIT 1;";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final HomeCache cache = new HomeCache();
	private final ConnectionProvider connectionProvider;

	public HomeProvider(ConnectionProvider connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public Optional<Home> getHomeByUserAndNumber(User user, int homeNumber) throws HeavenException
	{
		// Try to get home from cache
		Home home = cache.getHomeByUserAndNumber(user, homeNumber);
		if (home != null)
			return Optional.of(home);

		// Get home from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_HOME_BY_USER_AND_NUMBER))
		{
			ps.setInt(1, user.getId());
			ps.setInt(2, homeNumber);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return Optional.empty();

			home = new Home(rs);
			cache.addToCache(home);
			return Optional.of(home);
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_HOME_BY_USER_AND_NUMBER, ex);
			throw new DatabaseErrorException();
		}
	}

	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	public void invalidateCache(User user, int homeNumber)
	{
		cache.invalidateCache(user, homeNumber);
	}
}