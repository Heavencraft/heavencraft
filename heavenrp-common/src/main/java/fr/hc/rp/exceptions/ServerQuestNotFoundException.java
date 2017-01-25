package fr.hc.rp.exceptions;

import fr.hc.core.exceptions.HeavenException;

public class ServerQuestNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public ServerQuestNotFoundException(int id)
	{
		super("La quête {%1$s} n'existe pas.", id);
	}
}