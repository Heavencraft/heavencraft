package fr.hc.rp.db.bankaccounts;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAccount
{
	private final int id;
	private final int balance;

	public BankAccount(int generatedId)
	{
		id = generatedId;
		balance = 0;
	}

	// Available from package only
	BankAccount(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		balance = rs.getInt("balance");
	}

	public int getId()
	{
		return id;
	}

	public int getBalance()
	{
		return balance;
	}
}