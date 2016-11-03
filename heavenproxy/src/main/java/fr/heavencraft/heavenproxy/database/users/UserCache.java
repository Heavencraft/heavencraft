package fr.heavencraft.heavenproxy.database.users;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// package only
class UserCache
{
    private static Map<UUID, User> usersByUniqueId = new ConcurrentHashMap<UUID, User>();
    private static Map<String, User> usersByName = new ConcurrentHashMap<String, User>();

    public static User getUserByUniqueId(UUID uniqueId)
    {
        return usersByUniqueId.get(uniqueId);
    }

    public static User getUserByName(String name)
    {
        return usersByName.get(name);
    }

    public static void addToCache(User user)
    {
        usersByUniqueId.put(user.getUniqueId(), user);
        usersByName.put(user.getName(), user);
    }

    public static void invalidateCache(User user)
    {
        System.out.println("Invalidate cache : User [" + user.getName() + "]");

        usersByUniqueId.remove(user.getUniqueId());
        usersByName.remove(user.getName());
    }
}