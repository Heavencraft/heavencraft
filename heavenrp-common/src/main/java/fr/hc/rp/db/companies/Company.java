package fr.hc.rp.db.companies;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import fr.hc.core.db.users.User;

public class Company
{
	private final int id;
	private final String name;
	private final String tag;
	private final int bankAccountId;
	private final boolean hasBankAccount;
	private final Collection<Integer> employers = new HashSet<Integer>();
	private final Collection<Integer> employees = new HashSet<Integer>();

	// Available from package only
	Company(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		name = rs.getString("name");
		tag = rs.getString("tag");
		bankAccountId = rs.getInt("bank_account_id");
		hasBankAccount = !rs.wasNull();
	}

	// Available from package only
	Company(int id, String name, String tag) throws SQLException
	{
		this.id = id;
		this.name = name;
		this.tag = tag;
		bankAccountId = 0;
		hasBankAccount = false;
	}

	// Available from package only
	void addEmployer(int userId)
	{
		employers.add(userId);
	}

	// Available from package only
	void addEmployee(int userId)
	{
		employees.add(userId);
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getTag()
	{
		return tag;
	}

	public boolean hasBankAccount()
	{
		return hasBankAccount;
	}

	public int getBankAccountId()
	{
		return bankAccountId;
	}

	public boolean isEmployer(User user)
	{
		return employers.contains(user.getId());
	}

	public boolean isEmployee(User user)
	{
		return employees.contains(user.getId());
	}

	public boolean isEmployeeOrEmployer(User user)
	{
		final int userId = user.getId();
		return employees.contains(userId) || employers.contains(userId);
	}

	public Collection<Integer> getEmployers()
	{
		return employers;
	}

	public Collection<Integer> getEmployees()
	{
		return employees;
	}
}