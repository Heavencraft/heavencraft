package fr.hc.rp.db.towns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.bankaccounts.BankAccount;
import fr.hc.rp.exceptions.TownNotFoundException;

public class TownProvider
{
	private static final String SELECT_TOWN_BY_NAME = "SELECT * FROM towns WHERE name = ? LIMIT 1;";
	private static final String SELECT_TOWN_BY_BANK_ACCOUNT_ID = "SELECT * FROM towns WHERE bank_account_id = ? LIMIT 1;";

	private static final String SELECT_MAYORS_BY_TOWN_ID = "SELECT user_id FROM towns_users WHERE town_id = ?;";
	private static final String SELECT_TOWNS_BY_USER_ID = "SELECT t.* FROM towns t, towns_users tu WHERE t.id = tu.town_id AND tu.user_id = ?;";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ConnectionProvider connectionProvider;
	private final TownCache cache = new TownCache();

	public TownProvider(ConnectionProvider connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public Town getTownByName(String name) throws HeavenException
	{
		// Try to get town from cache
		Town town = cache.getTownByName(name);
		if (town != null)
			return town;

		// Get town from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_TOWN_BY_NAME))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new TownNotFoundException(name);

			town = new Town(rs);
			loadMembers(town, connection);
			cache.addToCache(town);
			return town;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_TOWN_BY_NAME, ex);
			throw new DatabaseErrorException();
		}
	}

	public Town getTownByBankAccount(BankAccount account) throws HeavenException
	{
		final Optional<Town> optTown = getOptionalTownByBankAccount(account);
		if (!optTown.isPresent())
			throw new TownNotFoundException(account);
		return optTown.get();
	}

	public Optional<Town> getOptionalTownByBankAccount(BankAccount account) throws HeavenException
	{
		// Try to get town from cache
		Town town = cache.getTownByBankAccount(account);
		if (town != null)
			return Optional.of(town);

		// Get town from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_TOWN_BY_BANK_ACCOUNT_ID))
		{
			ps.setInt(1, account.getId());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return Optional.empty();

			town = new Town(rs);
			loadMembers(town, connection);
			cache.addToCache(town);
			return Optional.of(town);
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_TOWN_BY_BANK_ACCOUNT_ID, ex);
			throw new DatabaseErrorException();
		}
	}

	public Collection<Town> getTownsByUser(User user) throws HeavenException
	{
		// Get user from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_TOWNS_BY_USER_ID))
		{
			ps.setInt(1, user.getId());

			final ResultSet rs = ps.executeQuery();

			final Collection<Town> towns = new ArrayList<Town>();
			while (rs.next())
			{
				final Town town = new Town(rs);
				loadMembers(town, connection);
				cache.addToCache(town);
				towns.add(town);
			}
			return towns;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_TOWNS_BY_USER_ID, ex);
			throw new DatabaseErrorException();
		}
	}

	private void loadMembers(Town town, Connection connection) throws SQLException
	{
		try (PreparedStatement ps2 = connection.prepareStatement(SELECT_MAYORS_BY_TOWN_ID))
		{
			ps2.setInt(1, town.getId());

			final ResultSet rs2 = ps2.executeQuery();
			while (rs2.next())
				town.addMayor(rs2.getInt("user_id"));
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