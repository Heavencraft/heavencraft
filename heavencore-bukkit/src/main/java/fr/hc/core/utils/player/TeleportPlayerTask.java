package fr.hc.core.utils.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.hc.core.BukkitHeavenCore;
import fr.hc.core.HeavenCoreInstance;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.sync.SyncTask;
import fr.hc.core.utils.chat.ChatUtil;

public class TeleportPlayerTask implements SyncTask
{
	private final Player player;
	private final Location location;

	public TeleportPlayerTask(Player player, Location location)
	{
		this.player = player;
		this.location = location;

		// Bugfix : Load chunk before teleport to avoid player spawning in blocks.
		if (!location.getChunk().isLoaded())
			location.getChunk().load();
	}

	public TeleportPlayerTask(Player player, Entity destination)
	{
		this(player, destination.getLocation());
	}

	@Override
	public void execute() throws HeavenException
	{
		// Bugfix for foodlevel changing after teleport on a different world
		if (!player.getWorld().equals(location.getWorld()))
		{
			final int foodLevel = player.getFoodLevel();
			final float saturation = player.getSaturation();

			Bukkit.getScheduler().runTaskLater((BukkitHeavenCore) (HeavenCoreInstance.get()), new Runnable()
			{
				@Override
				public void run()
				{
					player.setFoodLevel(foodLevel);
					player.setSaturation(saturation);
				}
			}, 20);
		}

		if (player.isInsideVehicle() && player.getVehicle() instanceof AbstractHorse)
		{
			final AbstractHorse horse = (AbstractHorse) player.getVehicle();

			player.teleport(location);
			horse.teleport(player);

			ChatUtil.sendMessage(player, "Ton cheval a été téléporté avec toi. S'il n'est pas là, {déco reco}.");

			Bukkit.getScheduler().runTaskLater((BukkitHeavenCore) (HeavenCoreInstance.get()), new Runnable()
			{
				@Override
				public void run()
				{
					horse.setHealth(horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					horse.getPassengers().forEach(entity -> horse.removePassenger(entity));
					horse.addPassenger(player);
				}
			}, 20);
		}

		else
			player.teleport(location);
	}
}