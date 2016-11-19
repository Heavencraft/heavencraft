package fr.heavencraft.heavenproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;

public abstract class AbstractListener implements Listener
{
	protected final Logger log = LoggerFactory.getLogger(getClass());

	public AbstractListener()
	{
		ProxyServer.getInstance().getPluginManager().registerListener(HeavenProxyInstance.get(), this);
		log.info("Registered listener");
	}
}