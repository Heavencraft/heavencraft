package fr.hc.rp.db.bankaccounts;

import java.util.ArrayList;
import java.util.List;

import fr.hc.core.db.users.balance.UpdateUserBalanceQuery;
import fr.hc.core.tasks.queries.BatchQuery;
import fr.hc.core.tasks.queries.Query;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.users.RPUser;

public class BankAccountMoneyTransfertQuery extends BatchQuery
{
	public BankAccountMoneyTransfertQuery(BankAccount from, BankAccount to, int delta)
	{
		super(createQueries(from, to, delta));
	}

	private static List<Query> createQueries(BankAccount from, BankAccount to, int delta)
	{
		final List<Query> queries = new ArrayList<Query>();
		queries.add(new UpdateBankAccountBalanceQuery(from, -delta));
		queries.add(new UpdateBankAccountBalanceQuery(to, delta));
		return queries;
	}

	public BankAccountMoneyTransfertQuery(RPUser from, BankAccount to, int delta)
	{
		super(createQueries(from, to, delta));
	}

	private static List<Query> createQueries(RPUser from, BankAccount to, int delta)
	{
		final List<Query> queries = new ArrayList<Query>();
		queries.add(new UpdateUserBalanceQuery(from, -delta, HeavenRPInstance.get().getUserProvider()));
		queries.add(new UpdateBankAccountBalanceQuery(to, delta));
		return queries;
	}

	public BankAccountMoneyTransfertQuery(BankAccount from, RPUser to, int delta)
	{
		super(createQueries(from, to, delta));
	}

	private static List<Query> createQueries(BankAccount from, RPUser to, int delta)
	{
		final List<Query> queries = new ArrayList<Query>();
		queries.add(new UpdateBankAccountBalanceQuery(from, -delta));
		queries.add(new UpdateUserBalanceQuery(to, delta, HeavenRPInstance.get().getUserProvider()));
		return queries;
	}
}