package fr.heavencraft.heavenproxy;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class MemoryWatcherTask implements Runnable
{
	private static final int PERIOD = 1;

	public MemoryWatcherTask()
	{
		ProxyServer.getInstance().getScheduler()
				.schedule(HeavenProxy.getInstance(), this, PERIOD, PERIOD, TimeUnit.MINUTES);
	}

	@Override
	public void run()
	{
		double memUsed, memMax, memFree, percentageFree;

		memUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576D;
		memMax = Runtime.getRuntime().maxMemory() / 1048576D;
		memFree = memMax - memUsed;
		percentageFree = (100D / memMax) * memFree;

		String message = ChatColor.GOLD + "[";
		message += (percentageFree >= 60 ? ChatColor.GREEN : percentageFree >= 35 ? ChatColor.YELLOW : ChatColor.RED);

		int looped = 0;

		while (looped++ < (percentageFree / 5))
		{
			message += '#';
		}

		message += ChatColor.WHITE;

		while (looped++ <= 20)
		{
			message += "_";
		}

		message += ChatColor.GOLD + "]" + String.format("%,.2f", memFree) + "MB/" + String.format("%,.2f", memMax)
				+ "MB (" + String.format("%,.2f", percentageFree) + "%) free";

		ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText(message));
	}
}