package fr.hc.rp.db.companies;

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
import fr.hc.rp.exceptions.CompanyNotFoundException;

public class CompanyProvider
{
	private static final String SELECT_COMPANY_BY_ID = "SELECT * FROM companies WHERE id = ? LIMIT 1;";
	private static final String SELECT_COMPANY_BY_TAG = "SELECT * FROM companies WHERE tag = ? LIMIT 1;";
	private static final String INSERT_COMPANY = "INSERT INTO company (name, tag) VALUES (?, ?);";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ConnectionProvider connectionProvider;
	private final CompanyCache cache = new CompanyCache();

	public CompanyProvider(ConnectionProvider connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public Company getCompanyById(int id) throws HeavenException
	{
		// Try to get bank account from cache
		Company company = cache.getCompanyById(id);
		if (company != null)
			return company;

		// Get user from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_COMPANY_BY_ID))
		{
			ps.setInt(1, id);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new CompanyNotFoundException(id);

			company = new Company(rs);
			cache.addToCache(company);
			return company;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_COMPANY_BY_ID, ex);
			throw new DatabaseErrorException();
		}
	}

	public Company getCompanyByTag(String tag) throws HeavenException
	{
		// Try to get bank account from cache
		Company company = cache.getCompanyByTag(tag);
		if (company != null)
			return company;

		// Get user from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_COMPANY_BY_TAG))
		{
			ps.setString(1, tag);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new CompanyNotFoundException(tag);

			company = new Company(rs);
			cache.addToCache(company);
			return company;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_COMPANY_BY_TAG, ex);
			throw new DatabaseErrorException();
		}
	}

	public Company createCompany(String name, String tag) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_COMPANY, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, name);
			ps.setString(2, tag);
			ps.executeUpdate();

			final ResultSet generatedKeys = ps.getGeneratedKeys();
			generatedKeys.next();
			final Company company = new Company(generatedKeys.getInt(1), name, tag);
			cache.addToCache(company);
			return company;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", INSERT_COMPANY, ex);
			throw new DatabaseErrorException();
		}
	}

	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	public void invalidateCache(Company company)
	{
		cache.invalidateCache(company);
	}
}