package fr.hc.rp.db.stocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.rp.db.companies.Company;

// Available from package only
class StockCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, Stock> stocksById = new ConcurrentHashMap<Integer, Stock>();
	private final Map<Integer, Map<String, Stock>> stocksByNameByCompanyId = new ConcurrentHashMap<Integer, Map<String, Stock>>();

	public Stock getStockById(int id)
	{
		return stocksById.get(id);
	}

	public Stock getStockByCompanyAndName(Company company, String name)
	{
		final Map<String, Stock> stocksByName = stocksByNameByCompanyId.get(company.getId());
		if (stocksByName == null)
			return null;

		return stocksByName.get(name);
	}

	public void addToCache(Stock stock)
	{
		stocksById.put(stock.getId(), stock);

		Map<String, Stock> stocksByName = stocksByNameByCompanyId.get(stock.getCompanyId());
		if (stocksByName == null)
			stocksByNameByCompanyId.put(stock.getCompanyId(), stocksByName = new ConcurrentHashMap<>());
		stocksByName.put(stock.getName(), stock);
	}

	public void invalidateCache(Stock stock)
	{
		stocksById.remove(stock.getId());
		final Map<String, Stock> stocksByName = stocksByNameByCompanyId.get(stock.getCompanyId());
		if (stocksByName != null)
			stocksByName.remove(stock.getName());

		log.info("Invalidated stock cache for {}", stock);
	}
}