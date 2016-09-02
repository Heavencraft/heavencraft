package fr.hc.core.db.users.balance;

import java.util.ArrayList;
import java.util.List;

import fr.hc.core.db.users.UserProvider;
import fr.hc.core.tasks.queries.BatchQuery;
import fr.hc.core.tasks.queries.Query;

public class MoneyTransfertQuery extends BatchQuery
{
	public MoneyTransfertQuery(UserWithBalance from, UserWithBalance to, int delta,
			UserProvider<? extends UserWithBalance> userProvider)
	{
		super(createQueries(from, to, delta, userProvider));
	}

	private static List<Query> createQueries(UserWithBalance from, UserWithBalance to, int delta,
			UserProvider<? extends UserWithBalance> userProvider)
	{
		final List<Query> queries = new ArrayList<Query>();
		queries.add(new UpdateUserBalanceQuery(from, -delta, userProvider));
		queries.add(new UpdateUserBalanceQuery(to, delta, userProvider));
		return queries;
	}
}