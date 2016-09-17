package fr.hc.core.db.homes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.db.users.home.UserWithHome;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class SetHomeQuery implements Query
{
	private static final String QUERY = "REPLACE INTO homes SET user_id = ?, home_number = ?, world = ?, x = ?, y = ?, z = ?, pitch = ?, yaw = ?;";

	private final UserWithHome user;
	private final int homeNumber;
	private final String world;
	private final double x;
	private final double y;
	private final double z;
	private final float pitch;
	private final float yaw;
	private final HomeProvider homeProvider;

	public SetHomeQuery(UserWithHome user, int homeNumber, String world, double x, double y, double z, float pitch,
			float yaw, HomeProvider homeProvider)
	{
		this.user = user;
		this.homeNumber = homeNumber;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
		this.homeProvider = homeProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		// Home limit check
		if (homeNumber > user.getHomeNumber())
			throw new HeavenException("Vous n'avez pas de home %1$s.", homeNumber);

		try (Connection connection = homeProvider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, user.getId());
			ps.setInt(2, homeNumber);
			ps.setString(3, world);
			ps.setDouble(4, x);
			ps.setDouble(5, y);
			ps.setDouble(6, z);
			ps.setFloat(7, pitch);
			ps.setDouble(8, yaw);

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() == 0)
				throw new HeavenException("Vous fouillez dans votre bourse... Vous n'avez pas assez.");

			homeProvider.invalidateCache(user, homeNumber);
		}
	}
}