package fr.heavencraft.heavenproxy;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;

public abstract class AbstractListener implements Listener
{
    protected final ProxyLogger log = ProxyLogger.getLogger(getClass());

    public AbstractListener()
    {
        ProxyServer.getInstance().getPluginManager().registerListener(HeavenProxy.getInstance(), this);
        log.info("Registered listener");
    }
}