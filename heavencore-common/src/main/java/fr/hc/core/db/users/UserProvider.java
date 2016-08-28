package fr.hc.core.db.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;

public abstract class UserProvider<U extends User>
{
	private static final String SELECT_USER_BY_UUID = "SELECT * FROM users WHERE uuid = ? LIMIT 1;";
	private static final String SELECT_USER_BY_NAME = "SELECT * FROM users WHERE name = ? LIMIT 1;";
	private static final String INSERT_USER = "INSERT INTO users (uuid, name) VALUES (?, ?);";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ConnectionProvider connectionProvider;
	private final UsersCache<U> cache = new UsersCache<U>();
	private final UserFactory<U> factory;

	protected UserProvider(ConnectionProvider connectionProvider, UserFactory<U> factory)
	{
		this.connectionProvider = connectionProvider;
		this.factory = factory;
	}

	public Optional<U> getUserByUniqueId(UUID uniqueId) throws HeavenException
	{
		// Try to get user from cache
		U user = cache.getUserByUniqueId(uniqueId);
		if (user != null)
			return Optional.of(user);

		// Get user from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_UUID))
		{
			ps.setString(1, uniqueId.toString());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return Optional.empty();

			user = factory.newUser(rs);
			cache.addToCache(user);
			return Optional.of(user);
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_USER_BY_UUID, ex);
			throw new DatabaseErrorException();
		}
	}

	public Optional<U> getUserByName(String name) throws HeavenException
	{
		// Try to get user from cache
		U user = cache.getUserByName(name);
		if (user != null)
			return Optional.of(user);

		// Get user from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_NAME))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return Optional.empty();

			user = factory.newUser(rs);
			cache.addToCache(user);
			return Optional.of(user);
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_USER_BY_NAME, ex);
			throw new DatabaseErrorException();
		}
	}

	public void createUser(UUID uniqueId, String name) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_USER))
		{
			ps.setString(1, uniqueId.toString());
			ps.setString(2, name);

			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", INSERT_USER, ex);
			throw new DatabaseErrorException();
		}
	}

	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	public void invalidateCache(User user)
	{
		cache.invalidateCache(user);
	}
}