package fr.heavencraft.heavenproxy.listeners;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import fr.heavencraft.heavenproxy.AbstractListener;
import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.HeavenProxyInstance;
import fr.heavencraft.heavenproxy.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/*
 * Listener qui enregistre des informations dans la base de données (NSA)
 */
public class LogListener extends AbstractListener
{
	private enum Action
	{
		LOGIN, CHAT, COMMAND, MOD_HISTORY, LOGOUT
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPostLogin(final PostLoginEvent event)
	{
		ProxyServer.getInstance().getScheduler().runAsync(HeavenProxyInstance.get(), new Runnable()
		{
			@Override
			public void run()
			{
				final InetAddress address = event.getPlayer().getAddress().getAddress();

				log(event.getPlayer(), Action.LOGIN, address.toString() + " " + Utils.getExactLocation(address));
			}
		});
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDisconnect(PlayerDisconnectEvent event)
	{
		log(event.getPlayer(), Action.LOGOUT, "");
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onChat(ChatEvent event)
	{
		if (event.isCancelled())
			return;

		if (!(event.getSender() instanceof ProxiedPlayer))
			return;

		final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

		if (event.isCommand())
			log(player, Action.COMMAND, event.getMessage());
		else
			log(player, Action.CHAT, event.getMessage());
	}

	/*
	 * Historique de modération
	 */

	public static void addKick(final String playerName, final String kickedBy, String reason)
	{
		reason = reason.length() > 0 ? " (" + reason + ")" : "";

		log("", playerName, Action.MOD_HISTORY, "Kicked by " + kickedBy + reason + ".");
	}

	public static void addBan(final String playerName, final String bannedBy, String reason)
	{
		reason = reason.length() > 0 ? " (" + reason + ")" : "";

		log("", playerName, Action.MOD_HISTORY, "Banned by " + bannedBy + reason + ".");
	}

	public static void addUnban(final String playerName, final String unbannedBy)
	{
		log("", playerName, Action.MOD_HISTORY, "Unbanned by " + unbannedBy + ".");
	}

	/*
	 * Méthodes d'insertion dans la base de données
	 */

	private static void log(ProxiedPlayer player, Action action, String data)
	{
		final String server = player.getServer() == null ? "" : player.getServer().getInfo().getName();
		final String playerName = player.getName();

		log(server, playerName, action, data);
	}

	private static void log(final String server, final String playerName, final Action action, final String data)
	{
		try
		{
			final PreparedStatement ps = HeavenProxy.getConnection().prepareStatement(
					"INSERT INTO logs (timestamp, server, player, action, data) VALUES (?, ?, ?, ?, ?);");
			ps.setTimestamp(1, new Timestamp(new Date().getTime()));
			ps.setString(2, server);
			ps.setString(3, playerName);
			ps.setInt(4, action.ordinal());
			ps.setString(5, data);

			ps.executeUpdate();
			ps.close();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}