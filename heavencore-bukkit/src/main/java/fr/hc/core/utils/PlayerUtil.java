package fr.hc.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.hc.core.exceptions.PlayerNotConnectedException;
import fr.hc.core.utils.player.TeleportPlayerTask;

public class PlayerUtil
{
	public static Player getPlayer(String playerName) throws PlayerNotConnectedException
	{
		final Player player = Bukkit.getPlayer(playerName);

		if (player == null)
			throw new PlayerNotConnectedException(playerName);

		return player;
	}

	public static String getExactName(String playerName)
	{
		try
		{
			return getPlayer(playerName).getName();
		}
		catch (final PlayerNotConnectedException ex)
		{
			return playerName;
		}
	}

	@SuppressWarnings("deprecation")
	public static OfflinePlayer getOfflinePlayer(String playerName)
	{
		try
		{
			return getPlayer(playerName);
		}
		catch (final PlayerNotConnectedException ex)
		{
			return Bukkit.getOfflinePlayer(playerName);
		}
	}

	public static void teleportPlayer(Player player, Entity entity)
	{
		new TeleportPlayerTask(player, entity).schedule();
	}

	public static void teleportPlayer(Player player, Location location)
	{
		new TeleportPlayerTask(player, location).schedule();
	}

	public static String getUUID(Player player)
	{
		return player.getUniqueId().toString().replaceAll("-", "");
	}
}