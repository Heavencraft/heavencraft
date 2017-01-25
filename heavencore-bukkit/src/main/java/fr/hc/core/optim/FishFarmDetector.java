package fr.hc.core.optim;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.AbstractBukkitListener;

public class FishFarmDetector extends AbstractBukkitListener
{
	private static final String LOG_FISHFARM = "{} is suspect of using a fish farm.";
	private static final String KICK_FISHFARM = "Va pêcher ta mère!";

	private final Map<UUID, FishFarmStatistics> statisticsByPlayer = new HashMap<UUID, FishFarmStatistics>();

	public FishFarmDetector(JavaPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private final void onPlayerInteract(PlayerInteractEvent event)
	{
		// Player has a fishing rod
		if (!event.hasItem() || event.getItem().getType() != Material.FISHING_ROD)
			return;

		// Player is doing a right click
		final Action action = event.getAction();
		if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
			return;

		final Player player = event.getPlayer();
		final FishFarmStatistics statistics = statisticsByPlayer.get(player.getUniqueId());
		if (statistics == null)
		{
			statisticsByPlayer.put(player.getUniqueId(), new FishFarmStatistics());
			return;
		}

		if (statistics.isSuspect())
		{
			log.warn(LOG_FISHFARM, player.getName());
			player.kickPlayer(KICK_FISHFARM);
			return;
		}

		if (statistics.shouldReset())
			statistics.reset();
		else
			statistics.increment();
	}

	private class FishFarmStatistics
	{
		private static final int RESET_TIME = 10000; // 10s
		private static final int SUSPECT_TIME = 1000; // 1s
		private static final int SUSPECT_COUNTER = 500;

		private long lastUpdate = System.currentTimeMillis();
		private long counter = 1;

		public void reset()
		{
			lastUpdate = System.currentTimeMillis();
			counter = 1;
		}

		public void increment()
		{
			lastUpdate = System.currentTimeMillis();
			counter++;
		}

		public boolean shouldReset()
		{
			return System.currentTimeMillis() - lastUpdate > RESET_TIME;
		}

		public boolean isSuspect()
		{
			return System.currentTimeMillis() - lastUpdate < SUSPECT_TIME && counter > SUSPECT_COUNTER;
		}
	}
}