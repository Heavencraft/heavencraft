package fr.heavencraft.heavenproxy.listeners;

import java.util.ArrayList;
import java.util.List;

import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.Utils;
import fr.hc.core.exceptions.PlayerNotConnectedException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class SpyListener implements Listener
{
	private final static String MESSAGE_FORMAT = ChatColor.DARK_RED + "%1$s [%2$s] %3$s";
	private static List<String> _spies = new ArrayList<String>();

	public SpyListener()
	{
		HeavenProxy plugin = HeavenProxy.getInstance();
		plugin.getProxy().getPluginManager().registerListener(plugin, this);
	}

	public static void toggleSpy(String playerName)
	{
		if (_spies.contains(playerName))
			_spies.remove(playerName);
		else
			_spies.add(playerName);
	}

	@EventHandler
	public void onPostLogin(PostLoginEvent event)
	{
		_spies.remove(event.getPlayer().getName());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(ChatEvent event)
	{
		if (event.isCancelled() || !event.isCommand())
			return;

		if (!(event.getSender() instanceof ProxiedPlayer))
			return;

		String filter = event.getMessage().toLowerCase();

		if (filter.startsWith("/home") || filter.startsWith("/nexus") || filter.startsWith("/spawn")
				|| filter.startsWith("/poof") || filter.startsWith("/lit") || filter.startsWith("/me"))
			return;

		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		String message = String.format(MESSAGE_FORMAT, Utils.getPrefix(player), player.getName(),
				event.getMessage());

		for (String playerName : _spies)
		{
			try
			{
				Utils.getPlayer(playerName).sendMessage(TextComponent.fromLegacyText(message));
			}
			catch (PlayerNotConnectedException ex)
			{
			}
		}
	}
}
