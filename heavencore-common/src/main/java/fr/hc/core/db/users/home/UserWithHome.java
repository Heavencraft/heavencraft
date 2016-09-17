package fr.hc.core.db.users.home;

import fr.hc.core.db.users.User;

public interface UserWithHome extends User
{
	int getHomeNumber();
}