package fr.hc.core.worlds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.BukkitHeavenCore;

public class WorldSaveTask extends BukkitRunnable
{
	private static final long PERIOD = 1200; // 20 ticks * 60 seconds

	private final Logger log = LoggerFactory.getLogger(getClass());
	private int worldIndex = 0;

	public WorldSaveTask(BukkitHeavenCore plugin)
	{
		runTaskTimer(plugin, PERIOD, PERIOD);
	}

	@Override
	public void run()
	{
		final List<World> worlds = Bukkit.getWorlds();
		final World world = worlds.get(worldIndex++ % worlds.size());

		if (world.isAutoSave())
		{
			log.info("Disabling Bukkit world save for world {}.", world);
			world.setAutoSave(false);
		}

		log.info("Saving world {}...", world);
		world.save();
		log.info("World {} saved.", world);
	}
}