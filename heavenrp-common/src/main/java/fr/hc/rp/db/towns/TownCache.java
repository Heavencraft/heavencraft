package fr.hc.rp.db.towns;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.rp.db.bankaccounts.BankAccount;

// Available from package only
class TownCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<String, Town> townsByName = new ConcurrentHashMap<String, Town>();
	private final Map<Integer, Town> townsByBankAccountId = new ConcurrentHashMap<Integer, Town>();

	public Town getTownByName(String name)
	{
		return townsByName.get(name);
	}

	public Town getTownByBankAccount(BankAccount account)
	{
		return townsByBankAccountId.get(account.getId());
	}

	public void addToCache(Town town)
	{
		townsByName.put(town.getName(), town);
		townsByBankAccountId.put(town.getBankAccountId(), town);
	}

	public void invalidateCache(Town town)
	{
		townsByName.remove(town.getName());
		townsByBankAccountId.remove(town.getBankAccountId());

		log.info("Invalidated town cache for {}", town);
	}
}