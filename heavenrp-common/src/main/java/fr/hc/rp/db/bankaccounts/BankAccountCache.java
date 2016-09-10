package fr.hc.rp.db.bankaccounts;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.db.users.User;
import fr.hc.rp.db.companies.Company;
import fr.hc.rp.db.towns.Town;

class BankAccountCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, BankAccount> bankAccountsById = new HashMap<Integer, BankAccount>();
	private final Map<Integer, BankAccount> bankAccountsByCompany = new HashMap<Integer, BankAccount>();
	private final Map<Integer, BankAccount> bankAccountsByTown = new HashMap<Integer, BankAccount>();
	private final Map<Integer, BankAccount> bankAccountsByUser = new HashMap<Integer, BankAccount>();

	public BankAccount getBankAccountById(int id)
	{
		return bankAccountsById.get(id);
	}

	public BankAccount getBankAccountByCompany(Company company)
	{
		return bankAccountsByCompany.get(company.getId());
	}

	public BankAccount getBankAccountByTown(Town town)
	{
		return bankAccountsByTown.get(town.getId());
	}

	public BankAccount getBankAccountByUser(User user)
	{
		return bankAccountsByUser.get(user.getId());
	}

	public void addToCache(BankAccount bankAccount)
	{
		bankAccountsById.put(bankAccount.getId(), bankAccount);
	}

	public void addToCache(BankAccount bankAccount, Company company)
	{
		addToCache(bankAccount);
		bankAccountsByCompany.put(company.getId(), bankAccount);
	}

	public void addToCache(BankAccount bankAccount, Town town)
	{
		addToCache(bankAccount);
		bankAccountsByTown.put(town.getId(), bankAccount);
	}

	public void addToCache(BankAccount bankAccount, User user)
	{
		addToCache(bankAccount);
		bankAccountsByUser.put(user.getId(), bankAccount);
	}

	public void invalidateCache(BankAccount bankAccount)
	{
		while (bankAccountsById.values().remove(bankAccount))
			;
		while (bankAccountsByCompany.values().remove(bankAccount))
			;
		while (bankAccountsByTown.values().remove(bankAccount))
			;
		while (bankAccountsByUser.values().remove(bankAccount))
			;

		log.info("Invalidated bank account cache for {}", bankAccount);
	}
}