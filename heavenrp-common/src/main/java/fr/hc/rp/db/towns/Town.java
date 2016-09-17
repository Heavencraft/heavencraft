package fr.hc.rp.db.towns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import fr.hc.core.db.users.User;

public class Town
{
	private final int id;
	private final String name;
	private final int bankAccountId;
	private final boolean hasBankAccount;
	private final Collection<Integer> mayors = new HashSet<Integer>();

	// Available from package only
	Town(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		name = rs.getString("name");
		bankAccountId = rs.getInt("bank_account_id");
		hasBankAccount = !rs.wasNull();
	}

	// Available from package only
	void addMayor(int userId)
	{
		mayors.add(userId);
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public boolean hasBankAccount()
	{
		return hasBankAccount;
	}

	public int getBankAccountId()
	{
		return bankAccountId;
	}

	public boolean isMayor(User user)
	{
		return mayors.contains(user.getId());
	}
}