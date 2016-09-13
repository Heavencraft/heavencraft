package fr.hc.rp.exceptions;

import fr.hc.core.exceptions.HeavenException;

public class CompanyNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public CompanyNotFoundException(String name)
	{
		super("L'entreprise {%1$s} n'existe pas.", name);
	}
}
