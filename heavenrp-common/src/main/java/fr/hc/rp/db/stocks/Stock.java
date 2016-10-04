package fr.hc.rp.db.stocks;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.HeavenBlockLocation;

public class Stock
{
	private final int id;
	private final CompanyIdAndStockName companyIdAndStockName;
	private final HeavenBlockLocation location;

	// Available from package only
	Stock(int id, CompanyIdAndStockName companyIdAndStockName, HeavenBlockLocation location)
	{
		this.id = id;
		this.companyIdAndStockName = companyIdAndStockName;
		this.location = location;
	}

	// Available from package only
	Stock(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.companyIdAndStockName = new CompanyIdAndStockName("company_id", "name", rs);
		this.location = new HeavenBlockLocation("world", "x", "y", "z", rs);
	}

	public int getId()
	{
		return id;
	}

	public CompanyIdAndStockName getCompanyIdAndStockName()
	{
		return companyIdAndStockName;
	}

	public HeavenBlockLocation getLocation()
	{
		return location;
	}
}