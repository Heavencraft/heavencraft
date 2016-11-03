package fr.heavencraft.heavenproxy.jit;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.Utils;

public class ConnectPlayerTask implements Runnable
{
	private final ScheduledTask task;
	private final String playerName;
	private final ServerInfo server;

	public ConnectPlayerTask(String playerName, ServerInfo server)
	{
		task = ProxyServer.getInstance().getScheduler()
				.schedule(HeavenProxy.getInstance(), this, 10, 1, TimeUnit.SECONDS);

		this.playerName = playerName;
		this.server = server;
	}

	@Override
	public void run()
	{
		final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);

		// The player is not connected anymore
		if (player == null)
		{
			ProxyServer.getInstance().getScheduler().cancel(task);
			return;
		}

		// The player is already on the server
		if (player.getServer() != null && player.getServer().getInfo().getName().equals(server.getName()))
		{
			ProxyServer.getInstance().getScheduler().cancel(task);
			return;
		}

		// If the server is not started
		if (SystemHelper.isPortAvailable(server.getAddress().getPort()))
			return;

		player.connect(server, new Callback<Boolean>()
		{
			@Override
			public void done(Boolean result, Throwable error)
			{
				if (result)
				{
					Utils.sendMessage(player, "Vous avez été téléporté au monde demandé.");
					ProxyServer.getInstance().getScheduler().cancel(task);
				}
				else
				{
					Utils.sendMessage(player, "Une erreur s'est produite, merci de contacter un administrateur.");
					error.printStackTrace();
				}
			}
		});
	}
}
