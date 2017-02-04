package fr.hc.guard.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.hc.guard.db.regions.Region;

public class RegionCache
{
	private final Map<Integer, Region> regionsById = new HashMap<Integer, Region>();
	private final Map<String, Region> regionsByName = new HashMap<String, Region>();
	private final Map<String, Collection<Region>> regionsByWorld = new HashMap<String, Collection<Region>>();

	public Region getRegionById(int id)
	{
		return regionsById.get(id);
	}

	public Region getRegionByName(String name)
	{
		return regionsByName.get(name);
	}

	public Collection<Region> getRegionsInWorld(String world)
	{
		final Collection<Region> regionsInWorld = regionsByWorld.get(world.toLowerCase());
		return regionsInWorld != null ? new ArrayList<Region>(regionsInWorld) : new ArrayList<Region>();
	}

	public boolean regionExists(String name)
	{
		return regionsByName.containsKey(name.toLowerCase());
	}

	public void addToCache(Region region)
	{
		regionsById.put(region.getId(), region); // Id -> Region
		regionsByName.put(region.getName(), region); // Name -> Region

		// Add to "World -> Regions" cache
		Collection<Region> regions = regionsByWorld.get(region.getWorld());

		if (regions == null)
			regionsByWorld.put(region.getWorld(), regions = new ArrayList<Region>());

		regions.add(region);
	}

	public void removeFromCache(Region region)
	{
		// Remove from "Name -> Region" cache
		regionsByName.remove(region.getName());

		// Remove from "World -> Regions" cache
		regionsByWorld.get(region.getWorld()).remove(region);
	}
}