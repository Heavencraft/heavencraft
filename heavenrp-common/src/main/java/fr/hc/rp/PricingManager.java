package fr.hc.rp;

import java.util.Calendar;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.bankaccounts.BankAccount;

public class PricingManager
{
	private final HeavenRP plugin = HeavenRPInstance.get();

	// Heavencraft's bank account
	private static final int HEAVENCRAFT_BANK_ACCOUNT_ID = 1;

	public BankAccount getHeavencraftAccount() throws HeavenException
	{
		return plugin.getBankAccountProvider().getBankAccountById(HEAVENCRAFT_BANK_ACCOUNT_ID);
	}

	// First login of the day's reward
	private static final int FIRST_LOGIN_REWARD = 5;

	public int getUserFirstLoginReward()
	{
		return FIRST_LOGIN_REWARD;
	}

	public int getBankAccountFirstLoginReward(BankAccount account)
	{
		if (account.getBalance() >= 25000)
			return 25;
		else
			return (int) (account.getBalance() * 0.001d);
	}

	// Periodic gain of money
	private static final long MONEY_TASK_PERIOD_MINUTES = 10;
	private static final long MONEY_TASK_PERIOD_TICKS = MONEY_TASK_PERIOD_MINUTES * 60 * 20;

	public long getMoneyTaskPeriodTicks()
	{
		return MONEY_TASK_PERIOD_TICKS;
	}

	public int getMoneyTaskAmount()
	{
		final Calendar date = Calendar.getInstance();

		switch (date.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.SATURDAY:
			case Calendar.SUNDAY:
				return date.get(Calendar.HOUR_OF_DAY) < 18 ? 2 : 3;

			default:
				return date.get(Calendar.HOUR_OF_DAY) < 18 ? 1 : 2;
		}
	}

	// Costs
	private static final int COMPANY_CREATION_COST = 150;

	public int getCompanyCreationCost()
	{
		return COMPANY_CREATION_COST;
	}
}