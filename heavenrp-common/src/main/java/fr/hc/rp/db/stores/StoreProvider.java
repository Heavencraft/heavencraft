package fr.hc.rp.db.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.stocks.CompanyIdAndStockName;

public class StoreProvider
{
	private static final String SELECT_STORE_BY_ID = "SELECT * FROM stores WHERE id = ? LIMIT 1;";
	private static final String SELECT_STORE_BY_LOCATION = "SELECT * FROM stores WHERE world = ? AND x = ? AND y = ? AND z = ? LIMIT 1;";
	private static final String SELECT_STORE_BY_COMPANY_AND_NAME = "SELECT * FROM stores WHERE company_id = ? AND name = ? LIMIT 1;";
	private static final String INSERT_STORE = "INSERT INTO stores (company_id, name, quantity, price, is_buyer, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ConnectionProvider connectionProvider;
	private final StoreCache cache = new StoreCache();

	public StoreProvider(ConnectionProvider connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public Store getStoreById(int id) throws HeavenException
	{
		// Try to get stock from cache
		Store store = cache.getStoreById(id);
		if (store != null)
			return store;

		// Get stock from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_STORE_BY_ID))
		{
			ps.setInt(1, id);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new StoreNotFoundException(id);

			store = new Store(rs);
			cache.addToCache(store);
			return store;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_STORE_BY_ID, ex);
			throw new DatabaseErrorException();
		}
	}

	public Store getStoreByLocation(HeavenBlockLocation location) throws HeavenException
	{
		// Try to get stock from cache
		Store store = cache.getStoreByLocation(location);
		if (store != null)
			return store;

		// Get stock from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_STORE_BY_LOCATION))
		{
			ps.setString(1, location.getWorld());
			ps.setInt(2, location.getX());
			ps.setInt(3, location.getY());
			ps.setInt(4, location.getZ());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new StoreNotFoundException(location);

			store = new Store(rs);
			cache.addToCache(store);
			return store;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_STORE_BY_LOCATION, ex);
			throw new DatabaseErrorException();
		}
	}

	public Store getStoreByCompanyAndName(CompanyIdAndStockName companyIdAndStockName) throws HeavenException
	{
		// Try to get stock from cache
		Store store = cache.getStoreByCompanyIdAndStockName(companyIdAndStockName);
		if (store != null)
			return store;

		// Get stock from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_STORE_BY_COMPANY_AND_NAME))
		{
			ps.setInt(1, companyIdAndStockName.getCompanyId());
			ps.setString(2, companyIdAndStockName.getStockName());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new StoreNotFoundException(companyIdAndStockName);

			store = new Store(rs);
			cache.addToCache(store);
			return store;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_STORE_BY_COMPANY_AND_NAME, ex);
			throw new DatabaseErrorException();
		}
	}

	public Store createStore(CompanyIdAndStockName companyIdAndStockName, int quantity, int price, boolean isBuyer,
			HeavenBlockLocation location) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_STORE, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setInt(1, companyIdAndStockName.getCompanyId());
			ps.setString(2, companyIdAndStockName.getStockName());
			ps.setInt(3, quantity);
			ps.setInt(4, price);
			ps.setBoolean(5, isBuyer);
			ps.setString(6, location.getWorld());
			ps.setInt(7, location.getX());
			ps.setInt(8, location.getY());
			ps.setInt(9, location.getZ());
			ps.executeUpdate();

			final ResultSet generatedKeys = ps.getGeneratedKeys();
			generatedKeys.next();
			final Store store = new Store(generatedKeys.getInt(1), companyIdAndStockName, quantity, price, isBuyer,
					location);
			cache.addToCache(store);
			// TODO
			return store;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", INSERT_STORE, ex);
			throw new DatabaseErrorException();
		}
	}

	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	public void invalidateCache(Store store)
	{
		cache.invalidateCache(store);
	}
}