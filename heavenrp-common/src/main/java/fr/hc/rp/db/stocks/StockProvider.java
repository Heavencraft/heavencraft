package fr.hc.rp.db.stocks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.companies.Company;

public class StockProvider
{
	private static final String SELECT_STOCK_BY_ID = "SELECT * FROM stocks WHERE id = ? LIMIT 1;";
	private static final String SELECT_STOCK_BY_COMPANY_AND_NAME = "SELECT * FROM stocks WHERE company_id = ? AND name = ? LIMIT 1;";
	private static final String INSERT_STOCK = "INSERT INTO stocks (company_id, name, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?);";

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

	public Stock getStockByCompanyAndName(Company company, String name) throws HeavenException
	{
		// Try to get stock from cache
		Stock stock = cache.getStockByCompanyAndName(company, name);
		if (stock != null)
			return stock;

		// Get stock from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_STOCK_BY_COMPANY_AND_NAME))
		{
			ps.setInt(1, company.getId());
			ps.setString(2, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new StockNotFoundException(company, name);

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

	public Stock createStock(Company company, String name, String world, int x, int y, int z) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_STOCK, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setInt(1, company.getId());
			ps.setString(2, name);
			ps.setString(3, world);
			ps.setInt(4, x);
			ps.setInt(5, y);
			ps.setInt(6, z);
			ps.executeUpdate();

			final ResultSet generatedKeys = ps.getGeneratedKeys();
			generatedKeys.next();
			return new Stock(generatedKeys.getInt(1), company, name, world, x, y, z);
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