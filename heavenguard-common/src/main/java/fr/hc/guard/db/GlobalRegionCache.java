package fr.hc.guard.db;

import java.util.HashMap;
import java.util.Map;

public class GlobalRegionCache
{
	private final Map<String, GlobalRegion> globalRegionsByWorld = new HashMap<String, GlobalRegion>();

	public GlobalRegion getGlobalRegionByWorld(String world)
	{
		return globalRegionsByWorld.get(world);
	}

	public boolean globalRegionExists(String world)
	{
		return globalRegionsByWorld.containsKey(world);
	}

	public void addToCache(GlobalRegion region)
	{
		globalRegionsByWorld.put(region.getName(), region);
	}
}