package fr.hc.core.db.users.home;

import fr.hc.core.db.users.balance.UserWithBalance;

public interface UserWithHome extends UserWithBalance
{
	int getHomeNumber();
}