package fr.heavencraft.heavenproxy.motd;

import fr.heavencraft.heavenproxy.AbstractListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener extends AbstractListener
{
	private static final String FIRST_LINE = "§l§fHeaven§bcraft§r [1.10]\n";

	@EventHandler
	public void onProxyPing(ProxyPingEvent event)
	{
		String description = FIRST_LINE;

		description += getServerString("Semi-RP", "semirp") + " ";
		description += getServerString("Créatif", "creative") + " ";
		// description += getServerString("Créatif", "creative", "fun", "musee", "build") + " ";
		// description += getServerString("Skyblock", "skyblock") + " ";
		// description += getServerString("Survie", "origines", "ultrahard") + " ";
		// description += getServerString("Jeux", "infected", "tntrun", "mariokart", "paintball", "event");

		event.getResponse().setDescription(description);
	}

	private static String getServerString(String name, String... servers)
	{
		for (final String server : servers)
		{
			final ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server);

			if (serverInfo != null && serverInfo.getPlayers().size() != 0)
			{
				return ChatColor.GREEN + name;
			}
		}

		return ChatColor.RED + name;
	}
}