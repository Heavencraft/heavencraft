package fr.hc.core.users;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.db.users.UpdateUserLastLoginQuery;
import fr.hc.core.db.users.UpdateUserNameQuery;
import fr.hc.core.db.users.User;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.event.FirstLoginEvent;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.DateUtil;

public class UsersListener extends AbstractBukkitListener
{
	private final UserProvider<? extends User> userProvider;

	public UsersListener(JavaPlugin plugin, UserProvider<? extends User> userProvider)
	{
		super(plugin);
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
			final Optional<? extends User> optUser = userProvider.getOptionalUserByUniqueId(uniqueId);

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

			if (!DateUtil.isToday(user.getLastLogin()))
			{
				log.info("First connection of {}, sending FirstLoginEvent", user);
				Bukkit.getPluginManager().callEvent(new FirstLoginEvent(player, user));
			}

			new UpdateUserLastLoginQuery(user, userProvider).schedule();
		}
		catch (final HeavenException ex)
		{
			ex.printStackTrace();
		}
	}
}