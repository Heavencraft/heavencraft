package fr.hc.core.tasks.sync;

import org.bukkit.Bukkit;

import fr.hc.core.BukkitHeavenCore;
import fr.hc.core.HeavenCoreInstance;

public class BukkitSyncTaskExecutor extends AbstractSyncTaskExecutor
{
	public BukkitSyncTaskExecutor()
	{
		Bukkit.getScheduler().runTaskTimer((BukkitHeavenCore) HeavenCoreInstance.get(), this, 1, 1);
	}
}