package fr.heavencraft.heavenproxy;

import java.io.File;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Subdivision;

import fr.hc.core.exceptions.PlayerNotConnectedException;
import fr.heavencraft.heavenproxy.servers.Server;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utils
{
	private final static String BEGIN = "{";
	private final static String END = "}";
	private final static String GOLD = ChatColor.GOLD.toString();
	private final static String RED = ChatColor.RED.toString();

	public static ProxiedPlayer getPlayer(String name) throws PlayerNotConnectedException
	{
		final String lowerCaseName = name.toLowerCase();

		for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
			if (player.getName().toLowerCase().startsWith(lowerCaseName))
				return player;

		throw new PlayerNotConnectedException(name);
	}

	public static boolean IsConnected(String name)
	{
		try
		{
			getPlayer(name);
			return true;
		}
		catch (final PlayerNotConnectedException e)
		{
			return false;
		}
	}

	public static String getRealName(String name)
	{
		try
		{
			return getPlayer(name).getName();
		}
		catch (final PlayerNotConnectedException ex)
		{
			return name;
		}
	}

	/*
	 * Géolocalisation
	 */

	private static DatabaseReader _databaseReader;

	public static String getCountry(final InetAddress address)
	{
		try
		{
			if (_databaseReader == null)
				_databaseReader = new DatabaseReader.Builder(new File("GeoLite2-City.mmdb")).build();

			final CountryResponse response = _databaseReader.country(address);
			return response.getCountry().getNames().get("fr");
		}
		catch (final Throwable t)
		{
			t.printStackTrace();
			return "France";
		}
	}

	public static String getExactLocation(final InetAddress address)
	{
		try
		{
			if (_databaseReader == null)
				_databaseReader = new DatabaseReader.Builder(new File("GeoLite2-City.mmdb")).build();

			final CityResponse response = _databaseReader.city(address);

			String location = "";
			String tmp;

			// Ville
			if ((tmp = response.getCity().getName()) != null)
				location += tmp;

			// Département, région
			for (final Subdivision subdivision : response.getSubdivisions())
				if ((tmp = subdivision.getName()) != null)
					location += (location == "" ? "" : " ") + tmp;

			// Pays
			if ((tmp = response.getCountry().getName()) != null)
				location += (location == "" ? "" : " ") + tmp;

			return location;
		}
		catch (final Throwable t)
		{
			t.printStackTrace();
			return "";
		}
	}

	/*
	 * Kick
	 */

	public static void kickPlayer(ProxiedPlayer player, String reason)
	{
		player.disconnect(TextComponent.fromLegacyText(reason));
	}

	/*
	 * Send message to a player
	 */

	public static void sendMessage(CommandSender sender, String message)
	{
		if (sender != null)
		{
			message = GOLD + message.replace(BEGIN, RED).replace(END, GOLD);

			for (final String line : message.split("\n"))
				sender.sendMessage(TextComponent.fromLegacyText(line));
		}
	}

	public static void sendMessage(CommandSender sender, String message, Object... args)
	{
		sendMessage(sender, String.format(message, args));
	}

	/*
	 * Send message to all players
	 */

	public static void broadcastMessage(String message)
	{
		message = GOLD + message.replace(BEGIN, RED).replace(END, GOLD);

		for (final String line : message.split("\n"))
			ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText(line));
	}

	public static void broadcastMessage(String message, Object... args)
	{
		broadcastMessage(String.format(message, args));
	}

	public static void broadcast(String message, String permission)
	{
		for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
			if (player.hasPermission(permission))
				sendMessage(player, message);
	}

	public static String ArrayToString(String[] array, int start, String separator)
	{
		String result = "";

		for (int i = start; i != array.length; i++)
			result += (result == "" ? "" : separator) + array[i];

		return result;
	}

	public static String getPrefix(ProxiedPlayer player)
	{
		final String serverName = player.getServer().getInfo().getName();

		return Server.getUniqueInstanceByName(serverName).getPrefix();
	}

	public static boolean isInteger(String s)
	{
		try
		{
			Integer.parseInt(s);
			return true;
		}
		catch (final NumberFormatException ex)
		{
			return false;
		}
	}
}
