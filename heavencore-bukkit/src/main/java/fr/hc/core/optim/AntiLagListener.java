package fr.hc.core.optim;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.WaterMob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;

public class AntiLagListener extends AbstractBukkitListener
{
	private static final int ANIMALS_LIMIT = 50;
	private static final int MONSTER_LIMIT = 50;
	private static final int WATER_MOB_LIMIT = 5;
	private static final int AMBIENT_LIMIT = 5;

	public AntiLagListener(AbstractBukkitPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onCreatureSpawn(CreatureSpawnEvent event)
	{
		// BUGFIX : pour que la téléportation avec un cheval fonctionne.
		if (event.getEntityType() == EntityType.HORSE && event.getSpawnReason() == SpawnReason.DEFAULT)
			return;

		final Entity entity = event.getEntity();
		Class<?> clazz;

		if (entity instanceof Animals)
			clazz = Animals.class;
		else if (entity instanceof Monster)
			clazz = Monster.class;
		else if (entity instanceof WaterMob)
			clazz = WaterMob.class;
		else if (entity instanceof Ambient)
			clazz = Ambient.class;
		else
			return;

		final Chunk chunk = event.getLocation().getChunk();

		final int count = countEntities(chunk, clazz);
		final int limit = getLimit(clazz);

		if (count >= limit)
		{
			event.setCancelled(true);

			if (count > limit)
			{
				log.info("Chunk {},{},{} already contains {} {}", chunk.getWorld().getName(), chunk.getX(),
						chunk.getZ(), count, clazz.getSimpleName());
			}
		}
	}

	private static int countEntities(Chunk chunk, Class<?> clazz)
	{
		int count = 0;

		final World world = chunk.getWorld();
		final int chunkX = chunk.getX();
		final int chunkZ = chunk.getZ();

		for (int x = -1; x <= 1; x++)
			for (int z = -1; z <= 1; z++)
				for (final Entity entity : world.getChunkAt(chunkX + x, chunkZ + z).getEntities())
					if (clazz.isInstance(entity))
						count++;

		return count;
	}

	private static int getLimit(Class<?> clazz)
	{
		if (clazz.equals(Animals.class))
			return ANIMALS_LIMIT;
		else if (clazz.equals(Monster.class))
			return MONSTER_LIMIT;
		else if (clazz.equals(WaterMob.class))
			return WATER_MOB_LIMIT;
		else if (clazz.equals(Ambient.class))
			return AMBIENT_LIMIT;
		else
			return 0;
	}
}