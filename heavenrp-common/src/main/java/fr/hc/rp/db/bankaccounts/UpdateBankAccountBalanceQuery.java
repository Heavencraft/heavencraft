package fr.hc.rp.db.bankaccounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;
import fr.hc.rp.HeavenRPInstance;

public class UpdateBankAccountBalanceQuery implements Query
{
	private static final String QUERY = "UPDATE bank_accounts SET balance = balance + ? WHERE id = ? AND balance + ? >= 0 LIMIT 1;";

	private final BankAccountProvider bankAccountProvider = HeavenRPInstance.get().getBankAccountProvider();

	private final BankAccount bankAccount;
	private final int delta;

	public UpdateBankAccountBalanceQuery(BankAccount bankAccount, int delta)
	{
		this.bankAccount = bankAccount;
		this.delta = delta;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (delta == 0)
			return; // Nothing to do

		try (Connection connection = bankAccountProvider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, delta);
			ps.setInt(2, bankAccount.getId());
			ps.setInt(3, delta);

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() == 0)
				throw new HeavenException("Vous n'avez pas assez sur votre compte en banque.");

			bankAccountProvider.invalidateCache(bankAccount);
		}
	}
}