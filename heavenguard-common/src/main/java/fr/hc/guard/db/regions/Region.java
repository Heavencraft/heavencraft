package fr.hc.guard.db.regions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.User;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.guard.HeavenGuardInstance;
import fr.hc.guard.db.Flag;
import fr.hc.guard.db.FlagHandler;
import fr.hc.guard.db.RegionProvider;

public class Region
{
	private static final String LOAD_MEMBERS = "SELECT user_id, owner FROM regions_members WHERE region_id = ?;";

	private static final int NO_PARENT = 0;

	private final ConnectionProvider connectionProvider;
	private final RegionProvider regionProvider;

	private final int id;
	private int parentId;
	private final String name;

	private String world;
	private int minX;
	private int minY;
	private int minZ;
	private int maxX;
	private int maxY;
	private int maxZ;

	private final Collection<Integer> members = new HashSet<Integer>();
	private final Collection<Integer> owners = new HashSet<Integer>();
	private final FlagHandler flagHandler;

	public Region(ConnectionProvider connectionHandler, ResultSet rs, RegionProvider regionProvider) throws SQLException
	{
		this.connectionProvider = connectionHandler;
		this.regionProvider = regionProvider;

		id = rs.getInt("id");
		parentId = rs.getInt("parent_id");
		name = rs.getString("name");

		world = rs.getString("world");
		minX = rs.getInt("min_x");
		minY = rs.getInt("min_y");
		minZ = rs.getInt("min_z");
		maxX = rs.getInt("max_x");
		maxY = rs.getInt("max_y");
		maxZ = rs.getInt("max_z");

		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(LOAD_MEMBERS))
		{
			ps.setInt(1, id);

			try (ResultSet rs2 = ps.executeQuery())
			{
				while (rs2.next())
					(rs2.getBoolean("owner") ? owners : members).add(rs2.getInt("user_id"));
			}
		}

		flagHandler = new FlagHandler(connectionHandler, rs, this);
	}

	@Override
	public String toString()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public Optional<Region> getParent()
	{
		if (parentId == NO_PARENT)
			return Optional.empty();

		return regionProvider.getRegionById(parentId);
	}

	public String getWorld()
	{
		return world;
	}

	public int getMinX()
	{
		return minX;
	}

	public int getMinY()
	{
		return minY;
	}

	public int getMinZ()
	{
		return minZ;
	}

	public int getMaxX()
	{
		return maxX;
	}

	public int getMaxY()
	{
		return maxY;
	}

	public int getMaxZ()
	{
		return maxZ;
	}

	public boolean contains(String world, int x, int y, int z)
	{
		return this.world.equals(world) //
				&& containsSameWorld(x, y, z);
	}

	public boolean containsSameWorld(int x, int y, int z)
	{
		return minX <= x && x <= maxX //
				&& minY <= y && y <= maxY //
				&& minZ <= z && z <= maxZ;
	}

	public boolean isMember(User user, boolean owner)
	{
		if (owner)
			return owners.contains(user.getId());
		else
			return owners.contains(user.getId()) || members.contains(user.getId());
	}

	public Collection<User> getMembers(boolean owner) throws HeavenException
	{
		final UserProvider<? extends User> userProvider = HeavenGuardInstance.get().getUserProvider();
		final Collection<User> users = new ArrayList<User>();

		for (final int userId : (owner ? owners : members))
		{
			final Optional<? extends User> optUser = userProvider.getUserById(userId);
			if (!optUser.isPresent())
				throw new UserNotFoundException(userId);

			users.add(optUser.get());
		}

		return users;
	}

	public boolean canBuilt(User user)
	{
		// If this region is public
		if (getFlagHandler().getBooleanFlag(Flag.PUBLIC) == Boolean.TRUE)
			return true;

		// Members/Owners of this region can build there
		if (isMember(user, false))
			return true;

		// Players that can build in the parent region can also build there
		final Optional<Region> parent = getParent();
		if (parent.isPresent())
			return parent.get().canBuilt(user);

		return false;
	}

	public FlagHandler getFlagHandler()
	{
		return flagHandler;
	}

	/*
	 * Callback of queries modifying a region
	 */

	void applyRedefine(String world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
	{
		this.world = world;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	void applySetParent(Region parent)
	{
		parentId = parent.getId();
	}

	void applyAddMember(User user, boolean owner)
	{
		(owner ? owners : members).add(user.getId());
	}

	void applyRemoveMember(User user, boolean owner)
	{
		(owner ? owners : members).remove(user.getId());
	}
}