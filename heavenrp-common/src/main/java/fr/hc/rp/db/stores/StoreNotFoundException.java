package fr.hc.rp.db.stores;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.stocks.CompanyIdAndStockName;

public class StoreNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public static final String MESSAGE = "Le panneau de magasin {%1$s} n'existe pas.";

	public StoreNotFoundException(int id)
	{
		super(MESSAGE, id);
	}

	public StoreNotFoundException(HeavenBlockLocation location)
	{
		super(MESSAGE, location);
	}

	public StoreNotFoundException(CompanyIdAndStockName companyIdAndStockName)
	{
		super(MESSAGE, companyIdAndStockName);
	}
}