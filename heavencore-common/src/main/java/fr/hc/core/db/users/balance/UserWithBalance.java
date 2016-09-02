package fr.hc.core.db.users.balance;

import fr.hc.core.db.users.User;

public interface UserWithBalance extends User
{
	int getBalance();
}