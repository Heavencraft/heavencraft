package fr.hc.core;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBukkitListener implements Listener
{
	protected final Logger log = LoggerFactory.getLogger(getClass());

	public AbstractBukkitListener(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
}