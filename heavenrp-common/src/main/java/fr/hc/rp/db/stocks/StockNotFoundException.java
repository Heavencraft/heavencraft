package fr.hc.rp.db.stocks;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.exceptions.HeavenException;

public class StockNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public static final String MESSAGE = "Le coffre de magasin {%1$s} n'existe pas.";

	public StockNotFoundException(int id)
	{
		super(MESSAGE, id);
	}

	public StockNotFoundException(CompanyIdAndStockName companyIdAndStockName)
	{
		super(MESSAGE, companyIdAndStockName);
	}

	public StockNotFoundException(HeavenBlockLocation location)
	{
		super(MESSAGE, location);
	}
}