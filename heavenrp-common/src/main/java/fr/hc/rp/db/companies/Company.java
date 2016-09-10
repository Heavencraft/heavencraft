package fr.hc.rp.db.companies;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Company
{
	private final int id;
	private final String name;
	private final int bankAccountId;

	// Available from package only
	Company(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		name = rs.getString("name");
		bankAccountId = rs.getInt("bank_account_id");
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public int getBankAccountId()
	{
		return bankAccountId;
	}
}