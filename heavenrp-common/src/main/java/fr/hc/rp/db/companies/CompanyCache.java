package fr.hc.rp.db.companies;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.rp.db.bankaccounts.BankAccount;

// Available from package only
class CompanyCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, Company> companiesById = new ConcurrentHashMap<Integer, Company>();
	private final Map<String, Company> companiesByTag = new ConcurrentHashMap<String, Company>();
	private final Map<Integer, Company> companiesByBankAccountId = new ConcurrentHashMap<Integer, Company>();

	public Company getCompanyById(int id)
	{
		return companiesById.get(id);
	}

	public Company getCompanyByTag(String name)
	{
		return companiesByTag.get(name);
	}

	public Company getCompanyByBankAccount(BankAccount account)
	{
		return companiesByBankAccountId.get(account.getId());
	}

	public void addToCache(Company company)
	{
		companiesById.put(company.getId(), company);
		companiesByTag.put(company.getTag(), company);
		companiesByBankAccountId.put(company.getBankAccountId(), company);
	}

	public void invalidateCache(Company company)
	{
		companiesById.remove(company.getId());
		companiesByTag.remove(company.getTag());
		companiesByBankAccountId.remove(company.getBankAccountId());

		log.info("Invalidated company cache for {}", company);
	}
}