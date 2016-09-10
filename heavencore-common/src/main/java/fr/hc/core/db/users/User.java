package fr.hc.core.db.users;

import java.util.UUID;

public interface User
{
	int getId();

	UUID getUniqueId();

	String getName();
}