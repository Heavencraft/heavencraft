package fr.heavencraft.heavenproxy.chat;

import java.net.InetAddress;

import fr.hc.core.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.HeavenProxyInstance;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.database.users.User;
import fr.heavencraft.heavenproxy.database.users.UserProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatManager
{
	private final static String WELCOME_MESSAGE = " * %1$s (%2$s) vient de rejoindre Heavencraft. {Bienvenue !}";
	private final static String JOIN_MESSAGE = " * %1$s (%2$s) vient de se connecter.";
	private final static String QUIT_MESSAGE = " * %1$s s'est déconnecté.";
	private final static String RAGEQUIT_MESSAGE = " * %1$s a ragequit.";

	private final static String KICK_MESSAGE_WITHOUT_REASON = " * %1$s a été exclu du serveur par {%2$s}.";
	private final static String KICK_MESSAGE_WITH_REASON = " * %1$s a été exclu du serveur par {%2$s} : %3$s.";
	private final static String BAN_MESSAGE_WITHOUT_REASON = " * %1$s a été banni du serveur par {%2$s}.";
	private final static String BAN_MESSAGE_WITH_REASON = " * %1$s a été banni du serveur par {%2$s} : %3$s.";

	private final static String CHAT_MESSAGE = ChatColor.GRAY + "%1$s %2$s[%3$s]" + ChatColor.RESET + " %4$s";

	public static void sendJoinMessage(final String playerName, final InetAddress address, final boolean welcome)
	{
		ProxyServer.getInstance().getScheduler().runAsync(HeavenProxyInstance.get(), new Runnable()
		{
			@Override
			public void run()
			{
				final String location = Utils.getCountry(address);

				if (welcome)
					Utils.broadcastMessage(WELCOME_MESSAGE, playerName, location);
				else
					Utils.broadcastMessage(JOIN_MESSAGE, playerName, location);
			}
		});
	}

	public static void sendQuitMessage(final String playerName)
	{
		Utils.broadcastMessage(QUIT_MESSAGE, playerName);
	}

	public static void sendRagequitMessage(final String playerName)
	{
		Utils.broadcastMessage(RAGEQUIT_MESSAGE, playerName);
	}

	public static void sendKickMessage(final String playerName, final String kickedBy, final String reason)
	{
		if (reason.isEmpty())
			Utils.broadcastMessage(KICK_MESSAGE_WITHOUT_REASON, playerName, kickedBy);
		else
			Utils.broadcastMessage(KICK_MESSAGE_WITH_REASON, playerName, kickedBy, reason);
	}

	public static void sendBanMessage(final String playerName, final String bannedBy, final String reason)
	{
		if (reason.isEmpty())
			Utils.broadcastMessage(BAN_MESSAGE_WITHOUT_REASON, playerName, bannedBy);
		else
			Utils.broadcastMessage(BAN_MESSAGE_WITH_REASON, playerName, bannedBy, reason);
	}

	public static void sendChatMessage(final ProxiedPlayer player, final String message)
	{
		try
		{
			final User user = UserProvider.getUserByName(player.getName());

			final String color = user.getColor();
			final String prefix = Utils.getPrefix(player);

			final String chatMessage = String.format(CHAT_MESSAGE, prefix, color, player.getName(), message);

			ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText(chatMessage));
		}
		catch (final HeavenException ex)
		{
			ex.printStackTrace();
			Utils.sendMessage(player, ex.getMessage());
		}
	}
}