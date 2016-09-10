package fr.hc.rp.db.companies;

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

public class CompanyProvider
{
	private static final String SELECT_COMPANY_BY_NAME = "SELECT * FROM companies WHERE name = ? LIMIT 1;";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ConnectionProvider connectionProvider;
	private final CompanyCache cache = new CompanyCache();

	public CompanyProvider(ConnectionProvider connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public Company getCompanyByName(String name) throws HeavenException
	{
		// Try to get bank account from cache
		Company company = cache.getCompanyByName(name);
		if (company != null)
			return company;

		// Get user from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_COMPANY_BY_NAME))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new TownNotFoundException(name);

			company = new Company(rs);
			cache.addToCache(company);
			return company;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_COMPANY_BY_NAME, ex);
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