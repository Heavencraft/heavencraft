package fr.hc.core.exceptions;

public class UnexpectedErrorException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public static final String MESSAGE = "Un problème inattendu s'est produit, merci de contacter un administrateur.";

	public UnexpectedErrorException()
	{
		super(MESSAGE);
	}
}