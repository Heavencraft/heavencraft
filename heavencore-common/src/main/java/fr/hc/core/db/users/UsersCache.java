package fr.hc.core.db.users;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsersCache<U extends User>
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, U> usersById = new ConcurrentHashMap<Integer, U>();
	private final Map<UUID, U> usersByUniqueId = new ConcurrentHashMap<UUID, U>();
	private final Map<String, U> usersByName = new ConcurrentHashMap<String, U>();

	public U getUserById(int id)
	{
		return usersById.get(id);
	}

	public U getUserByUniqueId(UUID uniqueId)
	{
		return usersByUniqueId.get(uniqueId);
	}

	public U getUserByName(String name)
	{
		return usersByName.get(name);
	}

	public void addToCache(U user)
	{
		usersById.put(user.getId(), user);
		usersByUniqueId.put(user.getUniqueId(), user);
		usersByName.put(user.getName(), user);
	}

	public void invalidateCache(User user)
	{
		usersById.remove(user.getId());
		usersByUniqueId.remove(user.getUniqueId());
		usersByName.remove(user.getName());

		log.info("Invalidated user cache for {}", user);
	}
}