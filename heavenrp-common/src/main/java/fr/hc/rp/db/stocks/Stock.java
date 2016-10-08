package fr.hc.rp.db.stocks;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.HeavenBlockLocation;

public class Stock
{
	private final int id;
	private final CompanyIdAndStockName companyIdAndStockName;
	private final HeavenBlockLocation signLocation;
	private final HeavenBlockLocation chestLocation;

	// Available from package only
	Stock(int id, CompanyIdAndStockName companyIdAndStockName, HeavenBlockLocation signLocation,
			HeavenBlockLocation chestLocation)
	{
		this.id = id;
		this.companyIdAndStockName = companyIdAndStockName;
		this.signLocation = signLocation;
		this.chestLocation = chestLocation;
	}

	// Available from package only
	Stock(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.companyIdAndStockName = new CompanyIdAndStockName("company_id", "name", rs);
		this.signLocation = new HeavenBlockLocation("world", "sign_x", "sign_y", "sign_z", rs);
		this.chestLocation = new HeavenBlockLocation("world", "chest_x", "chest_y", "chest_z", rs);
	}

	public int getId()
	{
		return id;
	}

	public CompanyIdAndStockName getCompanyIdAndStockName()
	{
		return companyIdAndStockName;
	}

	public HeavenBlockLocation getSignLocation()
	{
		return signLocation;
	}

	public HeavenBlockLocation getChestLocation()
	{
		return chestLocation;
	}
}