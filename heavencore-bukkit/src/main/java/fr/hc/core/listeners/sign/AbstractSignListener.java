package fr.hc.core.listeners.sign;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;

public abstract class AbstractSignListener
{
	private final String tag;
	private final String permission;

	public AbstractSignListener(JavaPlugin plugin, String tag)
	{
		this(plugin, tag, null);
	}

	public AbstractSignListener(JavaPlugin plugin, String tag, String permission)
	{
		this.tag = "[" + tag + "]";
		this.permission = permission;

		Bukkit.getPluginManager().registerEvents(new InternalListener(), plugin);
	}

	protected abstract boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException;

	protected abstract void onSignClick(Player player, Sign sign) throws HeavenException;

	protected abstract boolean onSignBreak(Player player, Sign sign) throws HeavenException;

	private class InternalListener implements Listener
	{
		@EventHandler(ignoreCancelled = true)
		private void onSignChange(SignChangeEvent event)
		{
			if (event.getBlock().getType() != Material.WALL_SIGN)
				return;

			final Player player = event.getPlayer();

			if (player == null || !event.getLine(0).equalsIgnoreCase(tag))
				return;

			if (permission != null && !player.hasPermission(permission))
				return;

			try
			{
				if (onSignPlace(player, event))
				{
					event.setLine(0, ChatColor.GREEN + tag);
					ChatUtil.sendMessage(player, "Le panneau {%1$s} a été placé correctement.", tag);
					return;
				}
			}
			catch (final HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}

			event.setCancelled(true);
			event.getBlock().breakNaturally();
		}

		@EventHandler(ignoreCancelled = true)
		private void onPlayerInteract(PlayerInteractEvent event)
		{
			if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
				return;

			final Block block = event.getClickedBlock();

			if (block.getType() != Material.WALL_SIGN)
				return;

			final Sign sign = (Sign) block.getState();

			if (!sign.getLine(0).equals(ChatColor.GREEN + tag))
				return;

			final Player player = event.getPlayer();
			event.setCancelled(true);

			try
			{
				onSignClick(player, sign);
			}
			catch (final HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}

		@EventHandler(ignoreCancelled = true)
		private void onBlockBreak(BlockBreakEvent event)
		{
			final Block block = event.getBlock();

			if (block.getType() != Material.WALL_SIGN)
				return;

			final Sign sign = (Sign) block.getState();

			if (!sign.getLine(0).equals(ChatColor.GREEN + tag))
				return;

			final Player player = event.getPlayer();

			try
			{
				if (onSignBreak(player, sign))
				{
					ChatUtil.sendMessage(player, "Le panneau {%1$s} a été détruit correctement.", tag);
					return;
				}
			}
			catch (final HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}

			event.setCancelled(true);
		}
	}
}