package fr.hc.rp.db.stores;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.rp.db.stocks.CompanyIdAndStockName;

// Available from package only
class StoreCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, Store> storesById = new ConcurrentHashMap<Integer, Store>();
	private final Map<HeavenBlockLocation, Store> storesByLocation = new ConcurrentHashMap<HeavenBlockLocation, Store>();
	private final Map<CompanyIdAndStockName, Store> storesByCompanyAndStockName = new ConcurrentHashMap<CompanyIdAndStockName, Store>();

	public Store getStoreById(int id)
	{
		return storesById.get(id);
	}

	public Store getStoreByLocation(HeavenBlockLocation location)
	{
		return storesByLocation.get(location);
	}

	public Store getStoreByCompanyIdAndStockName(CompanyIdAndStockName companyIdAndStockName)
	{
		return storesByCompanyAndStockName.get(companyIdAndStockName);
	}

	public void addToCache(Store store)
	{
		storesById.put(store.getId(), store);
		storesByLocation.put(store.getLocation(), store);
		storesByCompanyAndStockName.put(store.getCompanyIdAndStockName(), store);
	}

	public void invalidateCache(Store store)
	{
		storesById.remove(store.getId());
		storesByLocation.remove(store.getLocation());
		storesByCompanyAndStockName.remove(store.getCompanyIdAndStockName());

		log.info("Invalidated store cache for {}", store);
	}
}