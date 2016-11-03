package fr.heavencraft.heavenproxy.exceptions;

public class UUIDNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public UUIDNotFoundException(String playerName)
	{
		super("Le joueur {%1$s} n'a pas d'uuid. Vous avez surement fait une faute de frappe.", playerName);
	}
}