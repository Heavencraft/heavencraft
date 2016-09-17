package fr.hc.core.exceptions;

import java.util.UUID;

public class UserNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public static final String MESSAGE = "Le joueur {%1$s} n'existe pas.";

	public UserNotFoundException(int id)
	{
		super(MESSAGE, id);
	}

	public UserNotFoundException(UUID uuid)
	{
		super(MESSAGE, uuid);
	}

	public UserNotFoundException(String name)
	{
		super(MESSAGE, name);
	}
}