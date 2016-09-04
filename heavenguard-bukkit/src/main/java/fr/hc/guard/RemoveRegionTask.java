package fr.hc.guard;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.guard.db.Flag;
import fr.hc.guard.db.RegionProvider;
import fr.hc.guard.db.regions.Region;

public class RemoveRegionTask extends BukkitRunnable
{
	private static final Logger log = LoggerFactory.getLogger(RemoveRegionTask.class);
	private static final long PERIOD = 200; // 20 * 10 ticks

	private final RegionProvider regionProvider;

	public RemoveRegionTask(BukkitHeavenGuard plugin)
	{
		regionProvider = plugin.getRegionProvider();

		runTaskTimer(plugin, PERIOD, PERIOD);
	}

	@Override
	public void run()
	{
		final Date now = new Date();

		for (final World world : Bukkit.getWorlds())
		{
			for (final Region region : regionProvider.getRegionsInWorld(world.getName()))
			{
				try
				{
					final Timestamp remove = region.getFlagHandler().getTimestampFlag(Flag.REMOVE_TIMESTAMP);
					if (remove != null && remove.before(now))
					{
						final String regionName = region.getName();

						final byte[] state = region.getFlagHandler().getByteArrayFlag(Flag.STATE);
						if (state != null)
						{
							WorldEditUtil.load(state, world,
									new Location(world, region.getMinX(), region.getMinY(), region.getMinZ()),
									new Location(world, region.getMaxX(), region.getMaxY(), region.getMaxZ()));
							log.info("Region %1$s removed.", regionName);
						}

						regionProvider.deleteRegion(regionName);
						log.info("Region {} removed.", regionName);
					}
				}
				catch (final HeavenException | IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
}