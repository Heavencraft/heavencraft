package fr.heavencraft.heavenproxy.exceptions;

public class SQLErrorException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public SQLErrorException()
	{
		super("Une erreur SQL s'est produite, merci de contacter un administrateur.");
	}
}