package fr.heavencraft.heavenproxy.chat;

import fr.heavencraft.heavenproxy.AbstractListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.event.EventHandler;

public class TabCompleteListener extends AbstractListener
{
    @EventHandler
    public void onTabComplete(TabCompleteEvent event)
    {
        if (event.isCancelled())
            return;

        final String cursor = event.getCursor();
        String playerName = cursor.substring(cursor.lastIndexOf(" ") + 1);
        playerName = playerName.toLowerCase();

        for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
            if (player.getName().toLowerCase().startsWith(playerName))
                event.getSuggestions().add(player.getName());
    }
}