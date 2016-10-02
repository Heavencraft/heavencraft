package fr.hc.rp.db.stocks;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.rp.db.companies.Company;

public class Stock
{
	private final int id;
	private final int companyId;
	private final String name;
	private final String world;
	private final int x;
	private final int y;
	private final int z;

	// Available from package only
	Stock(int id, Company company, String name, String world, int x, int y, int z)
	{
		this.id = id;
		this.companyId = company.getId();
		this.name = name;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// Available from package only
	Stock(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.companyId = rs.getInt("company_id");
		this.name = rs.getString("name");
		this.world = rs.getString("world");
		this.x = rs.getInt("x");
		this.y = rs.getInt("y");
		this.z = rs.getInt("z");
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public int getCompanyId()
	{
		return companyId;
	}

	public String getWorld()
	{
		return world;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getZ()
	{
		return z;
	}
}