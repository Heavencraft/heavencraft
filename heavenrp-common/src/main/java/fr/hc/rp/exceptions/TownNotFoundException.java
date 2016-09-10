package fr.hc.rp.exceptions;

import fr.hc.core.exceptions.HeavenException;

public class TownNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public TownNotFoundException(String name)
	{
		super("La ville {%1$s} n'existe pas.", name);
	}
}