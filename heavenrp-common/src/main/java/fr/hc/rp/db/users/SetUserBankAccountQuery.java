package fr.hc.rp.db.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;
import fr.hc.rp.db.bankaccounts.BankAccount;

public class SetUserBankAccountQuery implements Query
{
	private static final String QUERY = "UPDATE users SET bank_account_id = ? WHERE id = ? LIMIT 1;";

	private final RPUser user;
	private final BankAccount bankAccount;
	private final RPUserProvider userProvider;

	public SetUserBankAccountQuery(RPUser user, BankAccount bankAccount, RPUserProvider userProvider)
	{
		this.user = user;
		this.bankAccount = bankAccount;
		this.userProvider = userProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (user.getBankAccountId() == bankAccount.getId())
			return; // Nothing to do

		try (Connection connection = userProvider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, bankAccount.getId());
			ps.setInt(2, user.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			userProvider.invalidateCache(user);
		}
	}
}