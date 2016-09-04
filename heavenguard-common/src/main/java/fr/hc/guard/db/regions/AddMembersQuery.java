package fr.hc.guard.db.regions;

import java.util.ArrayList;
import java.util.List;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.User;
import fr.hc.core.tasks.queries.BatchQuery;
import fr.hc.core.tasks.queries.Query;

public class AddMembersQuery extends BatchQuery
{
	public AddMembersQuery(Region region, List<User> users, boolean owner, final ConnectionProvider connectionProvider)
	{
		super(createQueries(region, users, owner, connectionProvider));
	}

	private static List<Query> createQueries(Region region, List<User> users, boolean owner,
			ConnectionProvider connectionProvider)
	{
		final List<Query> queries = new ArrayList<Query>();

		for (final User user : users)
			queries.add(new AddMemberQuery(region, user, owner, connectionProvider));

		return null;
	}
}