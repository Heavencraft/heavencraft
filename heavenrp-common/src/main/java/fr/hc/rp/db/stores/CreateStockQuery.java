package fr.hc.rp.db.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;
import fr.hc.rp.db.bankaccounts.BankAccount;
import fr.hc.rp.db.bankaccounts.BankAccountProvider;

public class CreateStockQuery implements Query
{

	private static final String QUERY = "INSERT INTO stocks(owner_id, bank_account_id, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?);";

	private final BankAccountProvider provider;
	private final User user;
	private final BankAccount account;
	private final String world;
	private final int x;
	private final int y;
	private final int z;

	/**
	 * Creates a new stock
	 * 
	 * @param provider
	 * @param user
	 *            The owner
	 * @param account
	 *            Where the money goes
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public CreateStockQuery(BankAccountProvider provider, User user, BankAccount account, String world, int x, int y,
			int z)
	{
		this.user = user;
		this.account = account;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		// TODO check if account exist, and if user owns this account (company/player account)

		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, user.getId());
			ps.setInt(2, account.getId());
			ps.setString(3, this.world);
			ps.setInt(4, x);
			ps.setInt(5, y);
			ps.setInt(6, z);

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() != 1)
				throw new DatabaseErrorException();

		}

	}

}
