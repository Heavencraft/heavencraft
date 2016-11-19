package fr.heavencraft.heavenproxy.users;

import java.util.UUID;

import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.heavencraft.heavenproxy.AbstractListener;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.async.QueriesHandler;
import fr.heavencraft.heavenproxy.chat.ChatManager;
import fr.heavencraft.heavenproxy.database.users.UpdateUserLastLoginQuery;
import fr.heavencraft.heavenproxy.database.users.UpdateUserNameQuery;
import fr.heavencraft.heavenproxy.database.users.User;
import fr.heavencraft.heavenproxy.database.users.UserProvider;
import fr.heavencraft.heavenproxy.kick.KickCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.protocol.ProtocolConstants;

public class UsersListener extends AbstractListener
{
	// Connection address for staff members
	private static final String STAFF_ADDRESS = "licorne.heavencraft.fr";

	private static final String LOG_BAD_VERSION = "[onLogin] {} is not in 1.10/1.11.";
	private static final String KICK_BAD_VERSION = "§fHeaven§bcraft§r est en 1.10 et 1.11.\n\nMerci de vous connecter avec une de ces versions.";

	private static final String LOG_ROBINSON = "[onLogin] {} is in 1.11.";
	private static final String KICK_ROBINSON = "Bienvenue sur §fHeaven§bcraft§r !\n\nPour accéder au monde Robinson 1.11,\nconnectez-vous à l'adresse suivante:\n\nheavencraft.fr:25568\n\nPour accéder aux mondes habituels,\nconnectez-vous avec la version 1.10.\n\nL'équipe d'§fHeaven§bcraft§r";

	@EventHandler
	public void onLogin(LoginEvent event)
	{
		if (event.isCancelled())
			return;

		switch (event.getConnection().getVersion())
		{
			case ProtocolConstants.MINECRAFT_1_10:
				break;

			case ProtocolConstants.MINECRAFT_1_11:
				log.info(LOG_ROBINSON, event.getConnection().getName());

				event.setCancelled(true);
				event.setCancelReason(KICK_ROBINSON);
				break;

			default:
				log.info(LOG_BAD_VERSION, event.getConnection().getName());

				event.setCancelled(true);
				event.setCancelReason(KICK_BAD_VERSION);
				break;
		}
	}

	// Must be run with LOWEST priority, as it create or update the user
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPostLoginLowest(PostLoginEvent event) throws DatabaseErrorException
	{
		final ProxiedPlayer player = event.getPlayer();
		final UUID uniqueId = player.getUniqueId();
		final String name = player.getName();

		try
		{
			final User user = UserProvider.getUserByUniqueId(uniqueId);

			if (!name.equals(user.getName()))
				QueriesHandler.addQuery(new UpdateUserNameQuery(user, name));

			QueriesHandler.addQuery(new UpdateUserLastLoginQuery(user));
			ChatManager.sendJoinMessage(name, player.getAddress().getAddress(), false);
		}
		catch (final UserNotFoundException ex)
		{
			UserProvider.createUser(uniqueId, name);
			ChatManager.sendJoinMessage(name, player.getAddress().getAddress(), true);
		}
	}

	@EventHandler
	public void onPostLogin(PostLoginEvent event)
	{
		final ProxiedPlayer player = event.getPlayer();

		if (player.hasPermission("heavencraft.commands.modo"))
		{
			final String hostName = event.getPlayer().getPendingConnection().getVirtualHost().getHostName();

			if (!STAFF_ADDRESS.equals(hostName))
			{
				KickCommand.kickPlayer(player, "Heavencraft", "Authentification invalide");
				return;
			}

			Utils.sendMessage(player, ChatColor.GREEN + "Vous êtes membre du staff, VOTEZ !");
			Utils.sendMessage(player, ChatColor.GREEN + "http://www.mcserv.org/Heavencraftfr_3002.html");
			Utils.sendMessage(player, ChatColor.GREEN + "http://mc-topserv.net/top/serveur.php?serv=46");
		}
	}
}