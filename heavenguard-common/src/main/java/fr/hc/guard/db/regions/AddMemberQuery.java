package fr.hc.guard.db.regions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class AddMemberQuery implements Query
{
	private static final String QUERY = "INSERT INTO regions_members (region_id, user_id, owner) VALUES (?, ?, ?);";

	private final Region region;
	private final User user;
	private final boolean owner;

	private final ConnectionProvider connectionProvider;

	public AddMemberQuery(Region region, User user, boolean owner, ConnectionProvider connectionProvider)
	{
		this.region = region;
		this.user = user;
		this.owner = owner;
		this.connectionProvider = connectionProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (region.isMember(user, false))
			throw new HeavenException("Le joueur {%1$s} est déjà membre de la protection {%2$s}.", user, region);

		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, region.getId());
			ps.setInt(2, user.getId());
			ps.setBoolean(3, owner);

			if (ps.executeUpdate() != 1)
				throw new HeavenException("Impossible de mettre à jour la région.");
		}

		region.applyAddMember(user, owner);
	}
}