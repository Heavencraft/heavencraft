package fr.heavencraft.heavenproxy.motd;

import fr.heavencraft.heavenproxy.AbstractListener;
import fr.heavencraft.heavenproxy.servers.Server;
import fr.heavencraft.heavenproxy.utils.LocalComponentBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener extends AbstractListener
{
	private static final TextComponent FIRST_LINE = buildFirstLine();
	private static final String SEMIRP_TAG = "Semi-RP";
	private static final String CREATIVE_TAG = "Créatif";
	private static final String ROBINSON_TAG = "Robinson 1.11";

	@EventHandler
	public void onProxyPing(ProxyPingEvent event)
	{
		final ComponentBuilder builder = LocalComponentBuilder.get();

		builder.color(getServerColor(Server.SemiRP)).append(SEMIRP_TAG);
		builder.color(getServerColor(Server.Creative)).append(CREATIVE_TAG);
		builder.color(getServerColor(Server.Robinson)).append(ROBINSON_TAG);

		event.getResponse().setDescriptionComponent(new TextComponent(FIRST_LINE, new TextComponent(builder.create())));
	}

	private static ChatColor getServerColor(Server... servers)
	{
		for (final Server server : servers)
		{
			final ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getName());

			if (serverInfo != null && serverInfo.getPlayers().size() != 0)
				return ChatColor.GREEN;
		}

		return ChatColor.RED;
	}

	private static TextComponent buildFirstLine()
	{
		final ComponentBuilder builder = LocalComponentBuilder.get();
		builder.bold(true);
		builder.color(ChatColor.WHITE).append("Heaven").color(ChatColor.AQUA).append("craft").reset();
		builder.append(" [1.10]\n");
		return new TextComponent(builder.create());
	}
}