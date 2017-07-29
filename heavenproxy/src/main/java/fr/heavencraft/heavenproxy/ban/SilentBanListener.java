package fr.heavencraft.heavenproxy.ban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import fr.heavencraft.heavenproxy.AbstractListener;
import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.HeavenProxyInstance;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class SilentBanListener extends AbstractListener
{
	private static final Collection<String> banned = Arrays.asList(//
			"ef032164-34a0-475c-81b9-389a4d4a2b8f", // Sigmund_Frog
			"b07d767d-01fa-40e2-89b0-c4696a3ccb1d", // forrto
			"93f49a17-80f2-47bd-a815-4e2e3336bf83", // Firebest1993
			"a46f7918-8cbd-4ee7-9048-13eb00dc812a", // batmab56
			"48183646-2cfe-4847-80ec-5bd4381ead5e"); // FaMoUsxj2

	private final Collection<String> bannedAddresses = new HashSet<String>();

	private final HeavenProxy plugin = HeavenProxyInstance.get();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(LoginEvent event)
	{
		if (event.isCancelled())
			return;

		final String uuid = event.getConnection().getUniqueId().toString();
		final String address = event.getConnection().getAddress().getAddress().toString();

		if (banned.contains(uuid) || bannedAddresses.contains(address))
		{
			bannedAddresses.add(address);

			saveIntoDatabase(uuid, event.getConnection().getName(), address);

			event.setCancelReason(new TextComponent("java.net.ConnectException: Connection refused:"));
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onProxyPing(ProxyPingEvent event)
	{
		final String address = event.getConnection().getAddress().getAddress().toString();

		if (bannedAddresses.contains(address))
		{
			event.setResponse(new ServerPing());
		}
	}

	private static String REPLACE = "REPLACE INTO silent_ban SET uuid = ?, name = ?, address = ?";

	private void saveIntoDatabase(String uuid, String name, String address)
	{
		try (Connection connection = plugin.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(REPLACE))
		{
			ps.setString(1, uuid);
			ps.setString(2, name);
			ps.setString(3, address);

			ps.executeUpdate();
		}

		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}