package fr.hc.core.exceptions;

public class DatabaseErrorException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public DatabaseErrorException()
	{
		super("Une erreur s'est produite avec la base de données, merci de contacter un administrateur.");
	}
}