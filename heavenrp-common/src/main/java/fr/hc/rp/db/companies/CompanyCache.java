package fr.hc.rp.db.companies;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Available from package only
class CompanyCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, Company> companiesById = new ConcurrentHashMap<Integer, Company>();
	private final Map<String, Company> companiesByTag = new ConcurrentHashMap<String, Company>();

	public Company getCompanyById(int id)
	{
		return companiesById.get(id);
	}

	public Company getCompanyByTag(String name)
	{
		return companiesByTag.get(name);
	}

	public void addToCache(Company company)
	{
		companiesById.put(company.getId(), company);
		companiesByTag.put(company.getTag(), company);
	}

	public void invalidateCache(Company company)
	{
		companiesById.remove(company.getId());
		companiesByTag.remove(company.getTag());

		log.info("Invalidated company cache for {}", company);
	}
}