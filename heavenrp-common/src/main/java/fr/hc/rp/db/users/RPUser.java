package fr.hc.rp.db.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.db.users.AbstractUser;
import fr.hc.core.db.users.balance.UserWithBalance;
import fr.hc.core.db.users.home.UserWithHome;

public class RPUser extends AbstractUser implements UserWithBalance, UserWithHome
{
	private final int balance;
	private final int bankAccountId;
	private final boolean hasBankAccount;
	private final int homeNumber;

	// Available from package only
	RPUser(ResultSet rs) throws SQLException
	{
		super(rs);

		balance = rs.getInt("balance");
		bankAccountId = rs.getInt("bank_account_id");
		hasBankAccount = !rs.wasNull();
		homeNumber = rs.getInt("home_number");
	}

	@Override
	public int getBalance()
	{
		return balance;
	}

	@Override
	public int getHomeNumber()
	{
		return homeNumber;
	}

	public boolean hasBankAccount()
	{
		return hasBankAccount;
	}

	public int getBankAccountId()
	{
		return bankAccountId;
	}
}