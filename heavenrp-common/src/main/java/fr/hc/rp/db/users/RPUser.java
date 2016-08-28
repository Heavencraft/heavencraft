package fr.hc.rp.db.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.db.users.User;

public class RPUser extends User
{
	private final int balance;

	RPUser(ResultSet rs) throws SQLException
	{
		super(rs);

		balance = rs.getInt("balance");
	}

	public int getBalance()
	{
		return balance;
	}
}