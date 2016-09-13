package fr.heavencraft.heavenguard.bukkit.listeners;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;

import com.google.common.collect.Sets;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.HeavenGuardPermissions;

public class ProtectionPlayerListener extends AbstractBukkitListener
{

	private static final Collection<Material> VEHICULES = Sets.newHashSet(Material.BOAT, Material.MINECART,
			Material.STORAGE_MINECART, Material.POWERED_MINECART, Material.EXPLOSIVE_MINECART, Material.HOPPER_MINECART,
			Material.COMMAND_MINECART, Material.ARMOR_STAND);

	private final BukkitHeavenGuard plugin;

	public ProtectionPlayerListener(BukkitHeavenGuard plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	/*
	 * BlockEvent
	 */

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		if (!canBuildAt(event.getPlayer(), event.getBlock()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onBlockDamage(BlockDamageEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		if (!canBuildAt(event.getPlayer(), event.getBlock()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onBlockIgnite(BlockIgniteEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		if (event.getPlayer() == null)
			return;

		if (!canBuildAt(event.getPlayer(), event.getBlock()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onBlockPlace(BlockPlaceEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		if (!canBuildAt(event.getPlayer(), event.getBlock()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onSignChange(SignChangeEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		if (!canBuildAt(event.getPlayer(), event.getBlock()))
			event.setCancelled(true);
	}

	/*
	 * EntityEvent
	 */

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		final Entity defender = event.getEntity();

		if (defender instanceof Monster)
			return;

		log.debug(event.getClass().getSimpleName());

		final Entity damager = event.getDamager();
		Player player;

		if (damager instanceof Player)
			player = (Player) damager;
		else if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player)
			player = (Player) ((Projectile) damager).getShooter();
		else
			return;

		final Block block = defender.getLocation().getBlock();

		// PVP
		if (defender.getType() == EntityType.PLAYER)
		{
			if (!isPvp(player, block))
				event.setCancelled(true);
		}

		// PVE
		else
		{
			if (!canBuildAt(player, block))
				event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onHangingPlace(HangingPlaceEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		if (!canBuildAt(event.getPlayer(), event.getBlock().getRelative(event.getBlockFace())))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onHangingBreakByEntity(HangingBreakByEntityEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		final Player player = event.getRemover() instanceof Player ? (Player) event.getRemover() : null;
		final Block block = event.getEntity().getLocation().getBlock();

		if (!canBuildAt(player, block))
			event.setCancelled(true);
	}

	/*
	 * PlayerEvent
	 */

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onPlayerBucketFill(PlayerBucketFillEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		final Block block = event.getBlockClicked().getRelative(event.getBlockFace());

		if (!canBuildAt(event.getPlayer(), block))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		final Block block = event.getBlockClicked().getRelative(event.getBlockFace());

		if (!canBuildAt(event.getPlayer(), block))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onPlayerBedEnter(PlayerBedEnterEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		if (!canBuildAt(event.getPlayer(), event.getBed()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		log.debug(event.getClass().getSimpleName() + " " + event.getRightClicked().getType());

		if (!canBuildAt(event.getPlayer(), event.getRightClicked().getLocation().getBlock()))
			event.setCancelled(true);
	}

	// Because ARMOR_STAND doesn't fire PlayerInteractEntityEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event)
	{
		if (event.getRightClicked().getType() != EntityType.ARMOR_STAND)
			return;

		log.debug(event.getClass().getSimpleName() + " " + event.getRightClicked().getType());

		if (!canBuildAt(event.getPlayer(), event.getRightClicked().getLocation().getBlock()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onPlayerInteract(PlayerInteractEvent event)
	{

		switch (event.getAction())
		{
			case RIGHT_CLICK_BLOCK:
				onPlayerRightClickBlock(event);
				break;

			default:
				break;
		}

		if (event.isCancelled())
		{
			event.setUseInteractedBlock(Result.DENY);
			event.setUseItemInHand(Result.DENY);
		}
	}

	private void onPlayerRightClickBlock(PlayerInteractEvent event)
	{
		final Block block = event.getClickedBlock();

		log.debug("onPlayerRightClickBlock {}", block.getType().name());

		// Player can't place a Vehicule
		if (event.hasItem() && VEHICULES.contains(event.getItem().getType()))
		{
			if (!canBuildAt(event.getPlayer(), block))
				event.setCancelled(true);
		}

		else
		{
			switch (block.getType())
			{
				case ANVIL:
				case BEACON:
				case BREWING_STAND:
				case BURNING_FURNACE:
				case CAKE_BLOCK:
				case CHEST:
				case FURNACE:
				case JUKEBOX:
				case TRAPPED_CHEST:
				case ENDER_PORTAL_FRAME:
				case FLOWER_POT:
					// Redstone
				case DISPENSER:
				case NOTE_BLOCK:
				case DAYLIGHT_DETECTOR:
				case DAYLIGHT_DETECTOR_INVERTED:
				case HOPPER:
				case DROPPER:
				case DIODE:
				case DIODE_BLOCK_OFF:
				case DIODE_BLOCK_ON:
				case REDSTONE_COMPARATOR:
				case REDSTONE_COMPARATOR_OFF:
				case REDSTONE_COMPARATOR_ON:

					if (!canBuildAt(event.getPlayer(), block))
						event.setCancelled(true);
					break;

				default:
					break; // Right click is allowed
			}
		}
	}

	// @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	// private void onPlayerInteract(PlayerInteractEvent event)
	// {
	// try
	// {
	// switch (event.getAction())
	// {
	// case RIGHT_CLICK_BLOCK:
	// onPlayerRightClickBlock(event);
	// break;
	// case PHYSICAL:
	// onPlayerPhysical(event);
	// break;
	// default:
	// break;
	// }
	// }
	// catch (HeavenException ex)
	// {
	// ex.printStackTrace();
	// ChatUtil.sendMessage(event.getPlayer(), ex.getMessage());
	// }
	// }

	// @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	// private void onPlayerRightClickBlock(PlayerInteractEvent event) throws
	// SQLErrorException
	// {
	// Block block = event.getClickedBlock();
	//
	// switch (block.getType())
	// {
	// case ANVIL:
	// case BREWING_STAND:
	// case BURNING_FURNACE:
	// case CAKE_BLOCK:
	// case CHEST:
	// case DISPENSER:
	// case DROPPER:
	// case FURNACE:
	// case HOPPER:
	// case JUKEBOX:
	// case NOTE_BLOCK:
	// case TRAPPED_CHEST:
	// if (!canBuildAt(event.getPlayer(), block))
	// event.setCancelled(true);
	// break;
	// default:
	// break;
	// }
	//
	// }

	// @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	// private void onPlayerPhysical(PlayerInteractEvent event)
	// {
	// Block block = event.getClickedBlock();
	//
	// switch (block.getType())
	// {
	// case SOIL:
	// case PUMPKIN_STEM:
	// case MELON_STEM:
	// if (!canBuildAt(event.getPlayer(), block))
	// event.setCancelled(true);
	// break;
	// default:
	// break;
	// }
	// }

	/*
	 * VehiculeEvent
	 */

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onVehicleDamage(VehicleDamageEvent event)
	{
		log.debug(event.getClass().getSimpleName() + " " + event.getVehicle().getType());

		if (event.getAttacker() == null || event.getAttacker().getType() != EntityType.PLAYER)
			return;

		if (!canBuildAt((Player) event.getAttacker(), event.getVehicle().getLocation().getBlock()))
			event.setCancelled(true);
	}

	private boolean canBuildAt(Player player, Block block)
	{
		if (player.hasPermission(HeavenGuardPermissions.BYPASS))
			return true;

		try
		{
			final boolean result = canBuildAt(plugin.getUserProvider().getUserByUniqueId(player.getUniqueId()).get(),
					block);
			if (!result)
				ChatUtil.sendMessage(player, "Cet endroit est protégé.");
			return result;
		}
		catch (final HeavenException ex)
		{
			ChatUtil.sendMessage(player, ex.getMessage());
			return false;
		}
	}

	private boolean canBuildAt(User user, Block block)
	{
		final boolean result = plugin.getRegionManager().canBuildAt(user, //
				block.getWorld().getName(), block.getX(), block.getY(), block.getZ());

		return result;
	}

	private boolean isPvp(Player player, Block block)
	{

		final boolean result = plugin.getRegionManager().isPvp( //
				block.getWorld().getName(), block.getX(), block.getY(), block.getZ());

		if (!result)
			ChatUtil.sendMessage(player, "Cet endroit n'est pas PVP.");

		return result;
	}
}