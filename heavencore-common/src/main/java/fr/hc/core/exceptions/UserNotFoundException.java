package fr.hc.core.exceptions;

import java.util.UUID;

public class UserNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(int id)
	{
		super("Le joueur {%1$s} n'existe pas.", id);
	}

	public UserNotFoundException(UUID uuid)
	{
		super("Le joueur {%1$s} n'existe pas.", uuid);
	}

	public UserNotFoundException(String name)
	{
		super("Le joueur {%1$s} n'existe pas.", name);
	}
}