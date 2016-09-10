package fr.hc.rp.db.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.db.users.AbstractUser;
import fr.hc.core.db.users.balance.UserWithBalance;

public class RPUser extends AbstractUser implements UserWithBalance
{
	private final int balance;
	private final int bankAccountId;

	// Available from package only
	RPUser(ResultSet rs) throws SQLException
	{
		super(rs);

		balance = rs.getInt("balance");
		bankAccountId = rs.getInt("bank_account_id");
	}

	@Override
	public int getBalance()
	{
		return balance;
	}

	public int getBankAccountId()
	{
		return bankAccountId;
	}
}