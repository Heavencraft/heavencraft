package fr.hc.core.db.users;

import java.sql.Timestamp;
import java.util.UUID;

public interface User
{
	int getId();

	UUID getUniqueId();

	String getName();

	Timestamp getLastLogin();
}