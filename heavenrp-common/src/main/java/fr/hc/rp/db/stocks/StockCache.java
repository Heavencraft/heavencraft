package fr.hc.rp.db.stocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Available from package only
class StockCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, Stock> stocksById = new ConcurrentHashMap<Integer, Stock>();
	private final Map<CompanyIdAndStockName, Stock> stocksByNameByCompanyId = new ConcurrentHashMap<CompanyIdAndStockName, Stock>();

	public Stock getStockById(int id)
	{
		return stocksById.get(id);
	}

	public Stock getStockByCompanyAndName(CompanyIdAndStockName companyIdAndStockName)
	{
		return stocksByNameByCompanyId.get(companyIdAndStockName);
	}

	public void addToCache(Stock stock)
	{
		stocksById.put(stock.getId(), stock);
		stocksByNameByCompanyId.put(stock.getCompanyIdAndStockName(), stock);
	}

	public void invalidateCache(Stock stock)
	{
		stocksById.remove(stock.getId());
		stocksByNameByCompanyId.remove(stock.getCompanyIdAndStockName());

		log.info("Invalidated stock cache for {}", stock);
	}
}