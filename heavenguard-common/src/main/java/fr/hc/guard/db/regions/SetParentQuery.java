package fr.hc.guard.db.regions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class SetParentQuery implements Query
{
	private static final String QUERY = "UPDATE regions SET parent_id = ? WHERE id = ? LIMIT 1";

	private final Region region;
	private final Region parent;

	private final ConnectionProvider connectionProvider;

	public SetParentQuery(Region region, Region parent, ConnectionProvider connectionProvider)
	{
		this.region = region;
		this.parent = parent;
		this.connectionProvider = connectionProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			if (parent == null)
				ps.setNull(1, Types.INTEGER);
			else
				ps.setInt(1, parent.getId());
			ps.setInt(2, region.getId());

			if (ps.executeUpdate() != 1)
				throw new HeavenException("Impossible de mettre à jour la région.");
		}

		region.applySetParent(parent);
	}
}