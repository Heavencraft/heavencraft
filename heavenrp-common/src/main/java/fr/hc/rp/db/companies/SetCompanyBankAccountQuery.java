package fr.hc.rp.db.companies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;
import fr.hc.rp.db.bankaccounts.BankAccount;

public class SetCompanyBankAccountQuery implements Query
{
	private static final String QUERY = "UPDATE companies SET bank_account_id = ? WHERE id = ? LIMIT 1;";

	private final Company company;
	private final BankAccount bankAccount;
	private final CompanyProvider companyProvider;

	public SetCompanyBankAccountQuery(Company company, BankAccount bankAccount, CompanyProvider companyProvider)
	{
		this.company = company;
		this.bankAccount = bankAccount;
		this.companyProvider = companyProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (company.getBankAccountId() == bankAccount.getId())
			return; // Nothing to do

		try (Connection connection = companyProvider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, bankAccount.getId());
			ps.setInt(2, company.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			companyProvider.invalidateCache(company);
		}
	}
}
