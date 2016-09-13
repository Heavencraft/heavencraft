package fr.hc.rp.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.hc.core.exceptions.PlayerNotConnectedException;
import fr.hc.core.utils.chat.ChatUtil;

public class PlayerUtil 
{
	public static Player getPlayer(String playerName) throws PlayerNotConnectedException
	{
		@SuppressWarnings("deprecation")
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

	public static void teleportPlayer(Player player, Location location)
	{
		player.teleport(location);
	}
//
//	public static void teleportPlayer(Player player, Entity entity)
//	{
//		ActionsHandler.addAction(new TeleportPlayerAction(player, entity));
//	}
//
//	public static void teleportPlayer(Player player, Location location, String message, Object... args)
//	{
//		ActionsHandler.addAction(new TeleportPlayerAction(player, location)
//		{
//			@Override
//			public void onSuccess()
//			{
//				ChatUtil.sendMessage(player, message, args);
//			}
//		});
//	}

//	public static void teleportPlayer(Player player, Entity entity, String message, Object... args)
//	{
//		ActionsHandler.addAction(new TeleportPlayerAction(player, entity)
//		{
//			@Override
//			public void onSuccess()
//			{
//				ChatUtil.sendMessage(player, message, args);
//			}
//		});
//	}
	
	/**
	 * Returns the UUID of the player wihout "-"
	 * @param player
	 * @return
	 */
	public static String getUUID(Player player)
	{
		return player.getUniqueId().toString().replaceAll("-", "");
	}
}