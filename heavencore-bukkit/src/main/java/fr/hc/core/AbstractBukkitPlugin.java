package fr.hc.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBukkitPlugin extends JavaPlugin
{
	protected final Logger log = LoggerFactory.getLogger(getClass());
}