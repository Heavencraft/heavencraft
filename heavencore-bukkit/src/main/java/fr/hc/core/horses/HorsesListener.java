package fr.hc.core.horses;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.HorseInventory;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.BukkitHeavenCore;

@SuppressWarnings("deprecation")
public class HorsesListener extends AbstractBukkitListener
{
	public HorsesListener(BukkitHeavenCore plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onVehicleEnter(VehicleEnterEvent event)
	{
		if (event.getVehicle().getType() != EntityType.HORSE || event.getEntered().getType() != EntityType.PLAYER)
			return;

		final Horse horse = (Horse) event.getVehicle();
		final Player player = (Player) event.getEntered();

		if (!HorsesManager.canUse(horse, player))
		{
			HorsesManager.sendWarning(horse, player);
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void onInventoryOpen(InventoryOpenEvent event)
	{
		if (!(event.getInventory() instanceof HorseInventory))
			return;

		final Horse horse = (Horse) event.getInventory().getHolder();
		final Player player = (Player) event.getPlayer();

		if (!HorsesManager.canUse(horse, player))
		{
			HorsesManager.sendWarning(horse, player);
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if (event.getEntityType() != EntityType.HORSE)
			return;

		final Horse horse = (Horse) event.getEntity();
		final Entity damager = event.getDamager();
		Player player;

		if (damager instanceof Player)
			player = (Player) damager;
		else if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player)
			player = (Player) ((Projectile) damager).getShooter();
		else
			return;

		if (!HorsesManager.canUse(horse, player))
		{
			if (horse.getOwner() != null)
				log.info("%1$s tried to damage %2$s's horse", player.getName(), horse.getOwner().getName());

			event.setCancelled(true);
		}
	}
}