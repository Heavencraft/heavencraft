package fr.hc.rp.db.towns;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Available from package only
class TownCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<String, Town> townsByName = new ConcurrentHashMap<String, Town>();

	public Town getTownByName(String name)
	{
		return townsByName.get(name);
	}

	public void addToCache(Town town)
	{
		townsByName.put(town.getName(), town);
	}

	public void invalidateCache(Town town)
	{
		townsByName.remove(town.getName());

		log.info("Invalidated town cache for {}", town);
	}
}