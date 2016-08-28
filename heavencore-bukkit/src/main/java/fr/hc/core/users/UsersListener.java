package fr.hc.core.users;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.hc.core.db.users.UpdateUserNameQuery;
import fr.hc.core.db.users.User;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.exceptions.HeavenException;

public class UsersListener implements Listener
{
	private final UserProvider<? extends User> userProvider;

	public UsersListener(UserProvider<? extends User> userProvider)
	{
		this.userProvider = userProvider;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerLogin(PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		final UUID uniqueId = player.getUniqueId();
		final String name = player.getName();

		try
		{
			final Optional<? extends User> optUser = userProvider.getUserByUniqueId(uniqueId);

			if (!optUser.isPresent())
			{
				userProvider.createUser(uniqueId, name);
				return;
			}

			final User user = optUser.get();

			if (!name.equals(user.getName()))
			{
				new UpdateUserNameQuery(user, name, userProvider).schedule();
			}
		}
		catch (final HeavenException ex)
		{
			ex.printStackTrace();
		}
	}
}