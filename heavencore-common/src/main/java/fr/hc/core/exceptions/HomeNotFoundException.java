package fr.hc.core.exceptions;

import fr.hc.core.db.users.User;

public class HomeNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public HomeNotFoundException(int homeNumber)
	{
		super("Vous n'avez pas configuré votre {home %1$s}, faites {/sethome %1$s} pour le configurer.", homeNumber);
	}

	public HomeNotFoundException(User user, int homeNumber)
	{
		super("%1$s n'a pas configuré son {home %2$s}.", user, homeNumber);
	}
}