package fr.hc.rp;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.bankaccounts.BankAccount;

public class PricingManager
{
	private static final int HEAVENCRAFT_BANK_ACCOUNT_ID = 1;
	private static final int COMPANY_CREATION_COST = 150;

	private final HeavenRP plugin = HeavenRPInstance.get();

	public int getCompanyCreationCost()
	{
		return COMPANY_CREATION_COST;
	}

	public BankAccount getHeavencraftAccount() throws HeavenException
	{
		return plugin.getBankAccountProvider().getBankAccountById(HEAVENCRAFT_BANK_ACCOUNT_ID);
	}
}