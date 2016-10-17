package fr.hc.rp.db.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.stocks.CompanyIdAndStockName;
import fr.hc.rp.db.stocks.Stock;

public class StoreProvider
{
	private static final String SELECT_STORE_BY_ID = "SELECT * FROM stores WHERE id = ? LIMIT 1;";
	private static final String SELECT_STORE_BY_LOCATION = "SELECT * FROM stores WHERE world = ? AND x = ? AND y = ? AND z = ? LIMIT 1;";
	private static final String SELECT_STORES_BY_COMPANY_AND_NAME = "SELECT * FROM stores WHERE company_id = ? AND stock_name = ?;";
	private static final String INSERT_STORE = "INSERT INTO stores (stock_id, company_id, stock_name, quantity, price, is_buyer, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

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
		final Optional<Store> optStore = getOptionalStoreByLocation(location);
		if (!optStore.isPresent())
			throw new StoreNotFoundException(location);
		return optStore.get();
	}

	public Optional<Store> getOptionalStoreByLocation(HeavenBlockLocation location) throws DatabaseErrorException
	{
		// Try to get store from cache
		Store store = cache.getStoreByLocation(location);
		if (store != null)
			return Optional.of(store);

		// Get store from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_STORE_BY_LOCATION))
		{
			ps.setString(1, location.getWorld());
			ps.setInt(2, location.getX());
			ps.setInt(3, location.getY());
			ps.setInt(4, location.getZ());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return Optional.empty();

			store = new Store(rs);
			cache.addToCache(store);
			return Optional.of(store);
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_STORE_BY_LOCATION, ex);
			throw new DatabaseErrorException();
		}
	}

	public Collection<Store> getStoresByCompanyAndName(CompanyIdAndStockName companyIdAndStockName)
			throws DatabaseErrorException
	{
		// Get stock from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_STORES_BY_COMPANY_AND_NAME))
		{
			ps.setInt(1, companyIdAndStockName.getCompanyId());
			ps.setString(2, companyIdAndStockName.getStockName());

			final ResultSet rs = ps.executeQuery();

			final Collection<Store> stores = new ArrayList<Store>();
			while (rs.next())
			{
				final Store store = new Store(rs);
				cache.addToCache(store);
				stores.add(store);
			}
			return stores;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_STORES_BY_COMPANY_AND_NAME, ex);
			throw new DatabaseErrorException();
		}
	}

	public Store createStore(CompanyIdAndStockName companyIdAndStockName, int quantity, int price, boolean isBuyer,
			HeavenBlockLocation location) throws HeavenException
	{
		final Optional<Stock> optStock = HeavenRPInstance.get().getStockProvider()
				.getOptionalStockByCompanyAndName(companyIdAndStockName);

		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_STORE, Statement.RETURN_GENERATED_KEYS))
		{
			if (optStock.isPresent())
				ps.setInt(1, optStock.get().getId());
			else
				ps.setNull(1, Types.INTEGER);
			ps.setInt(2, companyIdAndStockName.getCompanyId());
			ps.setString(3, companyIdAndStockName.getStockName());
			ps.setInt(4, quantity);
			ps.setInt(5, price);
			ps.setBoolean(6, isBuyer);
			ps.setString(7, location.getWorld());
			ps.setInt(8, location.getX());
			ps.setInt(9, location.getY());
			ps.setInt(10, location.getZ());
			ps.executeUpdate();

			final ResultSet generatedKeys = ps.getGeneratedKeys();
			generatedKeys.next();
			final Store store = new Store(generatedKeys.getInt(1), optStock.isPresent() ? optStock.get() : null,
					companyIdAndStockName, quantity, price, isBuyer, location);
			cache.addToCache(store);
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

	public void invalidateCache(Stock stock)
	{
		cache.invalidateCache(stock);
	}
}