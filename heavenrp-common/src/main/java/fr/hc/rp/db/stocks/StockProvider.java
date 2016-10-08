package fr.hc.rp.db.stocks;

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
import fr.hc.rp.db.stores.StoreNotFoundException;

public class StockProvider
{
	private static final String SELECT_STOCK_BY_ID = "SELECT * FROM stocks WHERE id = ? LIMIT 1;";
	private static final String SELECT_STOCK_BY_COMPANY_AND_NAME = "SELECT * FROM stocks WHERE company_id = ? AND name = ? LIMIT 1;";
	private static final String SELECT_STOCK_BY_SIGN_LOCATION = "SELECT * FROM stocks WHERE world = ? AND sign_x = ? AND sign_y = ? AND sign_z = ? LIMIT 1;";
	private static final String SELECT_STOCK_BY_CHEST_LOCATION = "SELECT * FROM stocks WHERE world = ? AND chest_x = ? AND chest_y = ? AND chest_z = ? LIMIT 1;";
	private static final String INSERT_STOCK = "INSERT INTO stocks (company_id, name, world, sign_x, sign_y, sign_z, chest_x, chest_y, chest_z) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ConnectionProvider connectionProvider;
	private final StockCache cache = new StockCache();

	public StockProvider(ConnectionProvider connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public Stock getStockById(int id) throws HeavenException
	{
		// Try to get stock from cache
		Stock stock = cache.getStockById(id);
		if (stock != null)
			return stock;

		// Get stock from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_STOCK_BY_ID))
		{
			ps.setInt(1, id);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new StockNotFoundException(id);

			stock = new Stock(rs);
			cache.addToCache(stock);
			return stock;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_STOCK_BY_ID, ex);
			throw new DatabaseErrorException();
		}
	}

	public Stock getStockByCompanyAndName(CompanyIdAndStockName companyIdAndStockName) throws HeavenException
	{
		// Try to get stock from cache
		Stock stock = cache.getStockByCompanyAndName(companyIdAndStockName);
		if (stock != null)
			return stock;

		// Get stock from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_STOCK_BY_COMPANY_AND_NAME))
		{
			ps.setInt(1, companyIdAndStockName.getCompanyId());
			ps.setString(2, companyIdAndStockName.getStockName());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new StockNotFoundException(companyIdAndStockName);

			stock = new Stock(rs);
			cache.addToCache(stock);
			return stock;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_STOCK_BY_COMPANY_AND_NAME, ex);
			throw new DatabaseErrorException();
		}
	}

	public Stock getStockBySignLocation(HeavenBlockLocation signLocation) throws HeavenException
	{
		// Try to get stock from cache
		Stock stock = cache.getStockBySignLocation(signLocation);
		if (stock != null)
			return stock;

		// Get stock from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_STOCK_BY_SIGN_LOCATION))
		{
			ps.setString(1, signLocation.getWorld());
			ps.setInt(2, signLocation.getX());
			ps.setInt(3, signLocation.getY());
			ps.setInt(4, signLocation.getZ());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new StoreNotFoundException(signLocation);

			stock = new Stock(rs);
			cache.addToCache(stock);
			return stock;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_STOCK_BY_SIGN_LOCATION, ex);
			throw new DatabaseErrorException();
		}
	}

	public Stock getStockByChestLocation(HeavenBlockLocation chestLocation) throws HeavenException
	{
		// Try to get stock from cache
		Stock stock = cache.getStockByChestLocation(chestLocation);
		if (stock != null)
			return stock;

		// Get stock from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_STOCK_BY_CHEST_LOCATION))
		{
			ps.setString(1, chestLocation.getWorld());
			ps.setInt(2, chestLocation.getX());
			ps.setInt(3, chestLocation.getY());
			ps.setInt(4, chestLocation.getZ());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new StoreNotFoundException(chestLocation);

			stock = new Stock(rs);
			cache.addToCache(stock);
			return stock;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_STOCK_BY_SIGN_LOCATION, ex);
			throw new DatabaseErrorException();
		}
	}

	public Stock createStock(CompanyIdAndStockName companyIdAndStockName, HeavenBlockLocation signLocation,
			HeavenBlockLocation chestLocation) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_STOCK, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setInt(1, companyIdAndStockName.getCompanyId());
			ps.setString(2, companyIdAndStockName.getStockName());
			ps.setString(3, signLocation.getWorld());
			ps.setInt(4, signLocation.getX());
			ps.setInt(5, signLocation.getY());
			ps.setInt(6, signLocation.getZ());
			ps.setInt(7, chestLocation.getX());
			ps.setInt(8, chestLocation.getY());
			ps.setInt(9, chestLocation.getZ());
			ps.executeUpdate();

			final ResultSet generatedKeys = ps.getGeneratedKeys();
			generatedKeys.next();
			final Stock stock = new Stock(generatedKeys.getInt(1), companyIdAndStockName, signLocation, chestLocation);
			cache.addToCache(stock);
			// TODO: Update Stores once done to link to the chest
			return stock;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", INSERT_STOCK, ex);
			throw new DatabaseErrorException();
		}
	}

	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	public void invalidateCache(Stock stock)
	{
		cache.invalidateCache(stock);
	}
}