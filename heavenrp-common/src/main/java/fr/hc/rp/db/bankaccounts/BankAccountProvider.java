package fr.hc.rp.db.bankaccounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.companies.Company;
import fr.hc.rp.db.companies.SetCompanyBankAccountQuery;
import fr.hc.rp.db.towns.SetTownBankAccountQuery;
import fr.hc.rp.db.towns.Town;
import fr.hc.rp.db.users.RPUser;
import fr.hc.rp.db.users.SetUserBankAccountQuery;
import fr.hc.rp.exceptions.BankAccountNotFoundException;

public class BankAccountProvider
{
	private static final String SELECT_BANK_ACCOUNT_BY_ID = "SELECT * FROM bank_accounts WHERE id = ? LIMIT 1;";
	private static final String INSERT_BANK_ACCOUNT = "INSERT INTO bank_accounts VALUES ();";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final HeavenRP plugin = HeavenRPInstance.get();
	private final BankAccountCache cache = new BankAccountCache();

	public BankAccount getBankAccountByUser(RPUser user) throws HeavenException
	{
		if (user.hasBankAccount())
		{
			return getBankAccountById(user.getBankAccountId());
		}
		else
		{
			final BankAccount bankAccount = createBankAccount();
			new SetUserBankAccountQuery(user, bankAccount, plugin.getUserProvider()).schedule();
			return bankAccount;
		}
	}

	public BankAccount getBankAccountByCompany(Company company) throws HeavenException
	{
		if (company.hasBankAccount())
		{
			return getBankAccountById(company.getBankAccountId());
		}
		else
		{
			final BankAccount bankAccount = createBankAccount();
			new SetCompanyBankAccountQuery(company, bankAccount, plugin.getCompanyProvider()).schedule();
			return bankAccount;
		}
	}

	public BankAccount getBankAccountByTown(Town town) throws HeavenException
	{
		if (town.hasBankAccount())
		{
			return getBankAccountById(town.getBankAccountId());
		}
		else
		{
			final BankAccount bankAccount = createBankAccount();
			new SetTownBankAccountQuery(town, bankAccount, plugin.getTownProvider()).schedule();
			return bankAccount;
		}
	}

	public Map<BankAccount, String> getAllPermissionnedBankAccountForUser(RPUser user) throws HeavenException
	{
		final Map<BankAccount, String> accounts = new HashMap<BankAccount, String>();

		// BankAccount of user
		accounts.put(getBankAccountByUser(user), user.getName());

		// BankAccount of companies
		for (final Company company : plugin.getCompanyProvider().getCompaniesByUser(user))
			if (company.isEmployer(user))
				accounts.put(getBankAccountByCompany(company), company.getName());

		for (final Town town : plugin.getTownProvider().getTownsByUser(user))
			accounts.put(getBankAccountByTown(town), town.getName());

		return accounts;
	}

	public BankAccount getBankAccountById(int id) throws HeavenException
	{
		// Try to get bank account from cache
		BankAccount bankAccount = cache.getBankAccountById(id);
		if (bankAccount != null)
			return bankAccount;

		// Get user from database
		try (Connection connection = plugin.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BANK_ACCOUNT_BY_ID))
		{
			ps.setInt(1, id);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new BankAccountNotFoundException(id);

			bankAccount = new BankAccount(rs);
			cache.addToCache(bankAccount);
			return bankAccount;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_BANK_ACCOUNT_BY_ID, ex);
			throw new DatabaseErrorException();
		}
	}

	private BankAccount createBankAccount() throws HeavenException
	{
		try (Connection connection = plugin.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_BANK_ACCOUNT,
						Statement.RETURN_GENERATED_KEYS))
		{
			ps.executeUpdate();

			final ResultSet generatedKeys = ps.getGeneratedKeys();
			generatedKeys.next();
			return new BankAccount(generatedKeys.getInt(1));
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", INSERT_BANK_ACCOUNT, ex);
			throw new DatabaseErrorException();
		}
	}

	public ConnectionProvider getConnectionProvider()
	{
		return plugin.getConnectionProvider();
	}

	public void invalidateCache(BankAccount bankAccount)
	{
		cache.invalidateCache(bankAccount);
	}
}