package fr.hc.core.db.homes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.db.users.User;

class HomeCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, Map<Integer, Home>> homesByNumberByUser = new ConcurrentHashMap<Integer, Map<Integer, Home>>();

	public Home getHomeByUserAndNumber(User user, int number)
	{
		final Map<Integer, Home> homesByNumber = homesByNumberByUser.get(user.getId());

		if (homesByNumber == null)
			return null;

		return homesByNumber.get(number);
	}

	public void addToCache(Home home)
	{
		Map<Integer, Home> homesByNumber = homesByNumberByUser.get(home.getUserId());

		if (homesByNumber == null)
			homesByNumberByUser.put(home.getUserId(), homesByNumber = new ConcurrentHashMap<Integer, Home>());

		homesByNumber.put(home.getHomeNumber(), home);
	}

	public void invalidateCache(User user, int homeNumber)
	{
		final Map<Integer, Home> homesByNumber = homesByNumberByUser.get(user.getId());

		if (homesByNumber != null)
			homesByNumber.remove(homeNumber);

		log.info("Invalidated home cache for {}-{}", user, homeNumber);
	}
}