package fr.hc.rp.exceptions;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.bankaccounts.BankAccount;

public class TownNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public TownNotFoundException(String name)
	{
		super("La ville {%1$s} n'existe pas.", name);
	}

	public TownNotFoundException(BankAccount account)
	{
		super("Aucune ville n'est relié au compte {%1$s}.", account);
	}
}