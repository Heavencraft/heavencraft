package fr.heavencraft.heavenproxy.ban;

import java.util.UUID;

import fr.heavencraft.heavenproxy.AbstractListener;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.event.EventHandler;

public class BanListener extends AbstractListener
{
	private static final String LOG_BANNED = "[onLogin] Account {} is banned : {}";
	private static final BaseComponent CANCEL_REASON = new TextComponent("Vous êtes banni d'Heavencraft.\n\n");

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
			event.setCancelReason(CANCEL_REASON, new TextComponent(reason));

			log.info(LOG_BANNED, uuid, reason);
		}
	}
}