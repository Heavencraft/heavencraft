package fr.hc.rp.db.companies;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Available from package only
class CompanyCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<String, Company> companiesByName = new ConcurrentHashMap<String, Company>();

	public Company getCompanyByName(String name)
	{
		return companiesByName.get(name);
	}

	public void addToCache(Company company)
	{
		companiesByName.put(company.getName(), company);
	}

	public void invalidateCache(Company company)
	{
		companiesByName.remove(company.getName());

		log.info("Invalidated company cache for {}", company);
	}
}