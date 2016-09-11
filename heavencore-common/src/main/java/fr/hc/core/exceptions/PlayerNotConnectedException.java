package fr.hc.core.exceptions;

public class PlayerNotConnectedException extends HeavenException{
	private static final long serialVersionUID = 1L;

	public PlayerNotConnectedException(String name)
	{
		super("Le joueur {%1$s} n'est pas connecté.", name);
	}
}