package fr.hc.guard.db.regions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class RemoveMemberQuery implements Query
{
	private static final String QUERY = "DELETE FROM regions_members WHERE region_id = ? AND user_id = ? AND owner = ? LIMIT 1;";

	private final Region region;
	private final User user;
	private final boolean owner;

	private final ConnectionProvider connectionProvider;

	public RemoveMemberQuery(Region region, User user, boolean owner, ConnectionProvider connectionProvider)
	{
		this.region = region;
		this.user = user;
		this.owner = owner;
		this.connectionProvider = connectionProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (!region.isMember(user, owner))
			throw new HeavenException("Le joueur {%1$s} n'est pas {%2$s} de la protection {%3$s}.", user,
					owner ? "propriétaire" : "membre", region);

		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, region.getId());
			ps.setInt(2, user.getId());
			ps.setBoolean(3, owner);

			if (ps.executeUpdate() != 1)
				throw new HeavenException("Impossible de mettre à jour la région.");
		}

		region.applyRemoveMember(user, owner);
	}
}