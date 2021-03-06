package fr.hc.rp.exceptions;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.bankaccounts.BankAccount;

public class CompanyNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public static final String MESSAGE = "L'entreprise {%1$s} n'existe pas.";

	public CompanyNotFoundException(int id)
	{
		super(MESSAGE, id);
	}

	public CompanyNotFoundException(String name)
	{
		super(MESSAGE, name);
	}

	public CompanyNotFoundException(BankAccount account)
	{
		super("Aucune entreprise n'est liée au compte {%1$s}.", account);
	}
}