package fr.hc.guard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import fr.hc.core.db.users.User;
import fr.hc.guard.db.Flag;
import fr.hc.guard.db.RegionProvider;
import fr.hc.guard.db.regions.Region;

public class RegionManager
{
	private final RegionProvider regionProvider;

	public RegionManager(RegionProvider regionProvider)
	{
		this.regionProvider = regionProvider;
	}

	public Collection<Region> getRegionsAtLocationWithoutParents(String world, int x, int y, int z)
	{
		final Collection<Region> regions = regionProvider.getRegionsAtLocation(world, x, y, z);

		if (regions.size() > 1)
		{
			final Collection<Region> toRemove = new ArrayList<Region>();

			for (final Region region : regions)
			{
				for (final Region parent : getAllParents(region))
				{
					if (regions.contains(parent))
					{
						toRemove.add(parent);
					}
				}
			}

			regions.removeAll(toRemove);
		}

		return regions;
	}

	private Collection<Region> getAllParents(Region region)
	{
		final Optional<Region> parent = region.getParent();

		if (!parent.isPresent())
			return new ArrayList<Region>();

		final Collection<Region> parents = getAllParents(parent.get());
		parents.add(parent.get());
		return parents;
	}

	public boolean canBuildAt(User user, String world, int x, int y, int z)
	{
		final Collection<Region> regions = getRegionsAtLocationWithoutParents(world, x, y, z);

		// If there are regions here
		if (regions.size() != 0)
		{
			// If a player can't build in one region, it can't build here
			for (final Region region : regions)
				if (!region.canBuilt(user))
					return false;

			return true;
		}

		// No regions here : the player can build if the world is public
		return regionProvider.getGlobalRegion(world).getFlagHandler().getBooleanFlag(Flag.PUBLIC);
	}

	public boolean isProtectedAgainstEnvironment(String world, int x, int y, int z)
	{
		final Collection<Region> regions = getRegionsAtLocationWithoutParents(world, x, y, z);

		// If there are regions here
		if (regions.size() != 0)
			return true;

		// No regions here : the block is protected if the world is not public
		return !regionProvider.getGlobalRegion(world).getFlagHandler().getBooleanFlag(Flag.PUBLIC);
	}

	public boolean areInSameRegion(String world, int x1, int y1, int z1, int x2, int y2, int z2)
	{
		final Collection<Region> regions1 = getRegionsAtLocationWithoutParents(world, x1, y1, z1);
		final Collection<Region> regions2 = getRegionsAtLocationWithoutParents(world, x2, y2, z2);

		return regions1.equals(regions2);
	}

	public boolean isPvp(String world, int x, int y, int z)
	{
		return getBooleanFlagValueAt(world, x, y, z, Flag.PVP);
	}

	public boolean canMobSpawn(String world, int x, int y, int z)
	{
		return getBooleanFlagValueAt(world, x, y, z, Flag.MOBSPAWNING);
	}

	public boolean canTeleport(String world, int x, int y, int z)
	{
		return getBooleanFlagValueAt(world, x, y, z, Flag.TELEPORT);
	}

	private boolean getBooleanFlagValueAt(String world, int x, int y, int z, Flag flag)
	{
		final Collection<Region> regions = getRegionsAtLocationWithoutParents(world, x, y, z);

		// If there are regions here
		if (regions.size() != 0)
		{
			boolean flagEnabled = false;
			boolean flagDisabled = false;

			for (final Region region : regions)
			{
				final Boolean flagValue = region.getFlagHandler().getBooleanFlag(flag);

				if (flagValue == null)
					continue;

				if (flagValue)
					flagEnabled = true;
				else
					flagDisabled = true;
			}

			if (flagEnabled || flagDisabled)
				return !flagDisabled && flagEnabled;
		}

		// No regions here (or no region with flag defined) -> use the default from the world
		return regionProvider.getGlobalRegion(world).getFlagHandler().getBooleanFlag(flag);
	}
}