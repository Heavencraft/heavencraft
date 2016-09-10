package fr.hc.rp.exceptions;

import fr.hc.core.exceptions.HeavenException;

public class BankAccountNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public BankAccountNotFoundException(int id)
	{
		super("Le compte en banque {%1$s} n'existe pas.", id);
	}
}