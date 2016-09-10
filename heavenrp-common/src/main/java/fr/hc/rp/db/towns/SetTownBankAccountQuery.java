package fr.hc.rp.db.towns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;
import fr.hc.rp.db.bankaccounts.BankAccount;

public class SetTownBankAccountQuery implements Query
{
	private static final String QUERY = "UPDATE towns SET bank_account_id = ? WHERE id = ? LIMIT 1;";

	private final Town town;
	private final BankAccount bankAccount;
	private final TownProvider townProvider;

	public SetTownBankAccountQuery(Town town, BankAccount bankAccount, TownProvider townProvider)
	{
		this.town = town;
		this.bankAccount = bankAccount;
		this.townProvider = townProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (town.getBankAccountId() == bankAccount.getId())
			return; // Nothing to do

		try (Connection connection = townProvider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, bankAccount.getId());
			ps.setInt(2, town.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			townProvider.invalidateCache(town);
		}
	}
}
