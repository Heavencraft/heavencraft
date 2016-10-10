package fr.hc.rp.db.stores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.rp.db.stocks.Stock;

// Available from package only
class StoreCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, Store> storesById = new ConcurrentHashMap<Integer, Store>();
	private final Map<HeavenBlockLocation, Store> storesByLocation = new ConcurrentHashMap<HeavenBlockLocation, Store>();

	public Store getStoreById(int id)
	{
		return storesById.get(id);
	}

	public Store getStoreByLocation(HeavenBlockLocation location)
	{
		return storesByLocation.get(location);
	}

	public void addToCache(Store store)
	{
		storesById.put(store.getId(), store);
		storesByLocation.put(store.getLocation(), store);
	}

	public void invalidateCache(Store store)
	{
		storesById.remove(store.getId());
		storesByLocation.remove(store.getLocation());

		log.info("Invalidated store cache for {}", store);
	}

	public void invalidateCache(Stock stock)
	{
		final Collection<Store> toInvalidate = new ArrayList<Store>();
		for (final Store store : storesById.values())
			if (store.hasStockId() && store.getStockId() == stock.getId())
				toInvalidate.add(store);
		for (final Store store : toInvalidate)
			invalidateCache(store);
	}
}