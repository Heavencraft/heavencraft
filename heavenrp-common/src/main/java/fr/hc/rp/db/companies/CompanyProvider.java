package fr.hc.rp.db.companies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import fr.hc.rp.exceptions.CompanyNotFoundException;

public class CompanyProvider
{
	private static final String SELECT_COMPANY_BY_ID = "SELECT * FROM companies WHERE id = ? LIMIT 1;";
	private static final String SELECT_COMPANY_BY_TAG = "SELECT * FROM companies WHERE tag = ? LIMIT 1;";
	private static final String SELECT_COMPANY_BY_BANK_ACCOUNT_ID = "SELECT * FROM companies WHERE bank_account_id = ? LIMIT 1;";
	private static final String SELECT_COMPANIES_BY_USER_ID = "SELECT c.* FROM companies c, companies_users cu WHERE c.id = cu.company_id AND cu.user_id = ?;";

	private static final String SELECT_MEMBERS_BY_COMPANY_ID = "SELECT user_id, employer FROM companies_users WHERE company_id = ?;";
	private static final String INSERT_COMPANY = "INSERT INTO companies (name, tag) VALUES (?, ?);";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ConnectionProvider connectionProvider;
	private final CompanyCache cache = new CompanyCache();

	public CompanyProvider(ConnectionProvider connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public Company getCompanyById(int id) throws HeavenException
	{
		// Try to get company from cache
		Company company = cache.getCompanyById(id);
		if (company != null)
			return company;

		// Get company from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_COMPANY_BY_ID))
		{
			ps.setInt(1, id);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new CompanyNotFoundException(id);

			company = new Company(rs);
			loadMembers(company, connection);
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
		final Optional<Company> optCompany = getOptionalCompanyByTag(tag);
		if (!optCompany.isPresent())
			throw new CompanyNotFoundException(tag);
		return optCompany.get();
	}

	public Optional<Company> getOptionalCompanyByTag(String tag) throws DatabaseErrorException
	{
		// Try to get company from cache
		Company company = cache.getCompanyByTag(tag);
		if (company != null)
			return Optional.of(company);

		// Get company from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_COMPANY_BY_TAG))
		{
			ps.setString(1, tag);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return Optional.empty();

			company = new Company(rs);
			loadMembers(company, connection);
			cache.addToCache(company);
			return Optional.of(company);
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_COMPANY_BY_TAG, ex);
			throw new DatabaseErrorException();
		}
	}

	public Company getCompanyByBankAccount(BankAccount account) throws HeavenException
	{
		final Optional<Company> optCompany = getOptionalCompanyByBankAccount(account);
		if (!optCompany.isPresent())
			throw new CompanyNotFoundException(account);
		return optCompany.get();
	}

	public Optional<Company> getOptionalCompanyByBankAccount(BankAccount account) throws DatabaseErrorException
	{
		// Try to get company from cache
		Company company = cache.getCompanyByBankAccount(account);
		if (company != null)
			return Optional.of(company);

		// Get company from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_COMPANY_BY_BANK_ACCOUNT_ID))
		{
			ps.setInt(1, account.getId());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return Optional.empty();

			company = new Company(rs);
			loadMembers(company, connection);
			cache.addToCache(company);
			return Optional.of(company);
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_COMPANY_BY_BANK_ACCOUNT_ID, ex);
			throw new DatabaseErrorException();
		}
	}

	public Collection<Company> getCompaniesByUser(User user) throws HeavenException
	{
		// Get user from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_COMPANIES_BY_USER_ID))
		{
			ps.setInt(1, user.getId());

			final ResultSet rs = ps.executeQuery();

			final Collection<Company> companies = new ArrayList<Company>();
			while (rs.next())
			{
				final Company company = new Company(rs);
				loadMembers(company, connection);
				cache.addToCache(company);
				companies.add(company);
			}
			return companies;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_COMPANY_BY_TAG, ex);
			throw new DatabaseErrorException();
		}
	}

	private void loadMembers(Company company, Connection connection) throws SQLException
	{
		try (PreparedStatement ps = connection.prepareStatement(SELECT_MEMBERS_BY_COMPANY_ID))
		{
			ps.setInt(1, company.getId());

			final ResultSet rs = ps.executeQuery();
			while (rs.next())
				if (rs.getBoolean("employer"))
					company.addEmployer(rs.getInt("user_id"));
				else
					company.addEmployee(rs.getInt("user_id"));
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