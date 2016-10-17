package fr.hc.rp.db.stocks;

import java.sql.ResultSet;
import java.sql.SQLException;

// TODO: find a better name
public class CompanyIdAndStockName
{
	private static final int HASHCODE_PRIME = 31;

	private final int companyId;
	private final String stockName;

	public CompanyIdAndStockName(int companyId, String stockName)
	{
		this.companyId = companyId;
		this.stockName = stockName;
	}

	public CompanyIdAndStockName(String companyId, String stockName, ResultSet rs) throws SQLException
	{
		this.companyId = rs.getInt(companyId);
		this.stockName = rs.getString(stockName);
	}

	@Override
	public String toString()
	{
		return companyId + "-" + stockName;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof CompanyIdAndStockName))
			return false;

		final CompanyIdAndStockName other = (CompanyIdAndStockName) obj;
		return companyId == other.companyId && stockName.equals(other.stockName);
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = HASHCODE_PRIME * result + companyId;
		result = HASHCODE_PRIME * result + (stockName != null ? stockName.hashCode() : 0);
		return result;
	}

	public int getCompanyId()
	{
		return companyId;
	}

	public String getStockName()
	{
		return stockName;
	}
}