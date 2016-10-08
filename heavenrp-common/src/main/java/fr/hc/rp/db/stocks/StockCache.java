package fr.hc.rp.db.stocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.HeavenBlockLocation;

// Available from package only
class StockCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, Stock> stocksById = new ConcurrentHashMap<Integer, Stock>();
	private final Map<CompanyIdAndStockName, Stock> stocksByNameByCompanyId = new ConcurrentHashMap<CompanyIdAndStockName, Stock>();
	private final Map<HeavenBlockLocation, Stock> stocksBySignLocation = new ConcurrentHashMap<HeavenBlockLocation, Stock>();
	private final Map<HeavenBlockLocation, Stock> stocksByChestLocation = new ConcurrentHashMap<HeavenBlockLocation, Stock>();

	public Stock getStockById(int id)
	{
		return stocksById.get(id);
	}

	public Stock getStockByCompanyAndName(CompanyIdAndStockName companyIdAndStockName)
	{
		return stocksByNameByCompanyId.get(companyIdAndStockName);
	}

	public Stock getStockBySignLocation(HeavenBlockLocation signLocation)
	{
		return stocksBySignLocation.get(signLocation);
	}

	public Stock getStockByChestLocation(HeavenBlockLocation chestLocation)
	{
		return stocksByChestLocation.get(chestLocation);
	}

	public void addToCache(Stock stock)
	{
		stocksById.put(stock.getId(), stock);
		stocksByNameByCompanyId.put(stock.getCompanyIdAndStockName(), stock);
		stocksBySignLocation.put(stock.getSignLocation(), stock);
		stocksByChestLocation.put(stock.getChestLocation(), stock);
	}

	public void invalidateCache(Stock stock)
	{
		stocksById.remove(stock.getId());
		stocksByNameByCompanyId.remove(stock.getCompanyIdAndStockName());
		stocksBySignLocation.remove(stock.getSignLocation());
		stocksByChestLocation.remove(stock.getChestLocation());

		log.info("Invalidated stock cache for {}", stock);
	}
}