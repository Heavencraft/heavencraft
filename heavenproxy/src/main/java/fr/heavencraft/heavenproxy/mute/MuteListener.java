package fr.heavencraft.heavenproxy.mute;

import fr.heavencraft.heavenproxy.AbstractListener;
import fr.heavencraft.heavenproxy.database.users.User;
import fr.heavencraft.heavenproxy.database.users.UserProvider;
import fr.hc.core.exceptions.HeavenException;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class MuteListener extends AbstractListener
{
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(ChatEvent event) throws HeavenException
    {
        if (event.isCancelled())
            return;

        if (!(event.getSender() instanceof ProxiedPlayer))
            return;

        final String message = event.getMessage().toLowerCase();
        final boolean isPrivateMessage = message.startsWith("/m ") || message.startsWith("/msg ")
                || message.startsWith("/t ") || message.startsWith("/tell ") || message.startsWith("/w ");

        // Si c'est une commande autre que /m, /me ou /send -> on fait rien
        if (event.isCommand() && !isPrivateMessage && !message.startsWith("/me ") && !message.startsWith("/envoyer "))
            return;

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        final User user = UserProvider.getUserByUniqueId(player.getUniqueId());

        // Si le joueur est mute
        if (user.isMuted())
        {
            event.setCancelled(true);
            return;
        }
    }
}