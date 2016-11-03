package fr.heavencraft.heavenproxy.servers;

import fr.heavencraft.heavenproxy.AbstractListener;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.event.EventHandler;

public class TitleListener extends AbstractListener
{
    @EventHandler
    public void onServerConnected(ServerConnectedEvent event)
    {
        final String serverName = event.getServer().getInfo().getName();
        Server.getUniqueInstanceByName(serverName).getTitle().send(event.getPlayer());
    }
}