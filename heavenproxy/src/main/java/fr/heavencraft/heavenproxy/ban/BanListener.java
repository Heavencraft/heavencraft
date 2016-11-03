package fr.heavencraft.heavenproxy.ban;

import java.util.UUID;

import fr.heavencraft.heavenproxy.AbstractListener;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.event.EventHandler;

public class BanListener extends AbstractListener
{
    private static final String LOG_BANNED = "[onLogin] Account %1$s is banned : %2$s";
    private static final String CANCEL_REASON = "Vous êtes banni d'Heavencraft.\n\n";

    @EventHandler
    public void onLogin(LoginEvent event)
    {
        if (event.isCancelled())
            return;

        final UUID uuid = event.getConnection().getUniqueId();
        final String reason = BanManager.getReason(uuid);

        if (reason != null)
        {
            event.setCancelled(true);
            event.setCancelReason(CANCEL_REASON + reason);

            log.info(LOG_BANNED, uuid, reason);
        }
    }
}