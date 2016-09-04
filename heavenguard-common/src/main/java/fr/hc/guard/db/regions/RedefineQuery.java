package fr.hc.guard.db.regions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class RedefineQuery implements Query
{
	private static final String QUERY = "UPDATE regions SET world = LOWER(?), min_x = ?, min_y = ?, min_z = ?, max_x = ?, max_y = ?, max_z = ? WHERE id = ? LIMIT 1";

	private final Region region;
	private final String world;
	private final int minX;
	private final int minY;
	private final int minZ;
	private final int maxX;
	private final int maxY;
	private final int maxZ;

	private final ConnectionProvider connectionProvider;

	public RedefineQuery(Region region, String world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
			ConnectionProvider connectionProvider)
	{
		this.region = region;
		this.world = world;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.connectionProvider = connectionProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setString(1, world);
			ps.setInt(2, minX);
			ps.setInt(3, minY);
			ps.setInt(4, minZ);
			ps.setInt(5, maxX);
			ps.setInt(6, maxY);
			ps.setInt(7, maxZ);
			ps.setInt(8, region.getId());

			if (ps.executeUpdate() != 1)
				throw new HeavenException("Impossible de mettre à jour la région.");
		}

		region.applyRedefine(world, minX, minY, minZ, maxX, maxY, maxZ);
	}
}