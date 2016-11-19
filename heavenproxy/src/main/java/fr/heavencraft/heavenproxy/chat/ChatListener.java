package fr.heavencraft.heavenproxy.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.heavencraft.heavenproxy.AbstractListener;
import fr.heavencraft.heavenproxy.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatListener extends AbstractListener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(ChatEvent event)
	{
		if (event.isCancelled())
			return;

		if (!(event.getSender() instanceof ProxiedPlayer))
			return;

		final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		String message = event.getMessage();

		if (event.isCommand())
		{
			// BUGFIX : pour que la console puisse voir les commandes
			log.info("[onPlayerChat] {} issued server command: {}", player.getName(), message);
			return;
		}

		// BUGFIX : pour que la banque du semi-rp fonctionne
		if (player.getServer().getInfo().getName().equalsIgnoreCase("semirp") && Utils.isInteger(message))
			return;

		if (player.hasPermission("heavencraft.chat.color"))
		{
			final Matcher matcher = Pattern.compile("\\&([0-9A-Fa-f])").matcher(message);
			message = matcher.replaceAll("§$1");
		}

		ChatManager.sendChatMessage(player, message);
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent event)
	{
		final String playerName = event.getPlayer().getName();
		final String reason = DisconnectReasonManager.getReason(playerName);

		if (reason == null)
			ChatManager.sendQuitMessage(playerName);
		else
		{
			final String[] data = reason.split("\\|", 3);

			if (data[0].equals("R"))
				ChatManager.sendRagequitMessage(playerName);
			else if (data[0].equals("K"))
				ChatManager.sendKickMessage(playerName, data[1], data[2]);
			else if (data[0].equals("B"))
				ChatManager.sendBanMessage(playerName, data[1], data[2]);
			else
				ChatManager.sendQuitMessage(playerName);
		}
	}
}