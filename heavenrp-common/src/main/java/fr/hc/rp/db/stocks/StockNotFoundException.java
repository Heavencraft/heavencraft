package fr.hc.rp.db.stocks;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.companies.Company;

public class StockNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public StockNotFoundException(int id)
	{
		super("Le coffre de magasin {%1$s} n'existe pas.", id);
	}

	public StockNotFoundException(Company company, String name)
	{
		super("Le coffre de magasin {%1$s} de {%2$s} n'existe pas.", name, company);
	}
}