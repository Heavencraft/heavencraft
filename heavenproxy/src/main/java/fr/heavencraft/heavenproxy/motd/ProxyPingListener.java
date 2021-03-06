package fr.heavencraft.heavenproxy.motd;

import fr.heavencraft.heavenproxy.AbstractListener;
import fr.heavencraft.heavenproxy.servers.Server;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener extends AbstractListener
{
	private static final ComponentBuilder FIRST_LINE = buildFirstLine();
	private static final String SEMIRP_TAG = "Semi-RP ";
	private static final String CREATIVE_TAG = "Créatif ";

	@EventHandler
	public void onProxyPing(ProxyPingEvent event)
	{
		final ComponentBuilder builder = new ComponentBuilder(FIRST_LINE);

		builder.append(SEMIRP_TAG).color(getServerColor(Server.SemiRP));
		builder.append(CREATIVE_TAG).color(getServerColor(Server.Creative));

		event.getResponse().setDescriptionComponent(new TextComponent(builder.create()));
	}

	private static ChatColor getServerColor(Server... servers)
	{
		for (final Server server : servers)
		{
			final ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getName());

			if (serverInfo != null && !serverInfo.getPlayers().isEmpty())
				return ChatColor.GREEN;
		}

		return ChatColor.RED;
	}

	private static ComponentBuilder buildFirstLine()
	{
		final ComponentBuilder builder = new ComponentBuilder("Heaven").color(ChatColor.WHITE).bold(true);
		builder.append("craft").color(ChatColor.AQUA);
		builder.append(" [1.12]\n").reset();
		return builder;
	}
}