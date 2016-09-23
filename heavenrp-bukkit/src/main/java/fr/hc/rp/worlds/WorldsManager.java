package fr.hc.rp.worlds;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;

import fr.hc.core.utils.WorldUtils;

public class WorldsManager
{
	public static final int RESOURCES_SIZE = 5000;

	private static Location _spawn;
	private static Location _spawnNether;
	private static Location _spawnTheEnd;

	static Random rnd = new Random();

	public static void init()
	{
		// Force difficulty of SRP to HARD
		getWorld().setDifficulty(Difficulty.HARD);

		if (!isLoaded("world_nether"))
		{
			final WorldCreator creator = new WorldCreator("world_nether");
			creator.environment(World.Environment.NETHER);
			creator.createWorld();
		}

		if (!isLoaded("world_resources"))
		{
			final WorldCreator creator = new WorldCreator("world_resources");
			creator.environment(World.Environment.NORMAL);
			// creator.seed(1423174317);
			// creator.seed(-5001027545084418649l);
			final World worldResources = creator.createWorld();
			worldResources.setDifficulty(Difficulty.NORMAL);
		}

		if (!isLoaded("world_the_end"))
		{
			final WorldCreator creator = new WorldCreator("world_the_end");
			creator.environment(World.Environment.THE_END);
			creator.createWorld();
		}

		_spawn = new Location(getWorld(), 351.5, 83, 1041.5, 90, 0);
		_spawnNether = new Location(getNether(), 96, 46, 176, 0, 0);
		_spawnTheEnd = new Location(getTheEnd(), 4.5D, 61D, 23.5D, 0F, 0F);
	}

	public static Location getSpawn()
	{
		return _spawn;
	}

	public static Location getSpawnNether()
	{
		return _spawnNether;
	}

	public static Location getSpawnTheEnd()
	{
		return _spawnTheEnd;
	}

	public static Location getResourcesSpawn()
	{
		int x;
		int z;
		do
		{
			x = rnd.nextInt(5000) - 2500;
			z = rnd.nextInt(5000) - 2500;
		}
		while ((getResources().getBiome(x, z) == Biome.OCEAN) || (getResources().getBiome(x, z) == Biome.DEEP_OCEAN));

		return WorldUtils.getSafeDestination(getResources(), x, z);
	}

	public static World getWorld()
	{
		return Bukkit.getWorld("world");
	}

	public static World getNether()
	{
		return Bukkit.getWorld("world_nether");
	}

	public static World getTheEnd()
	{
		return Bukkit.getWorld("world_the_end");
	}

	public static World getResources()
	{
		return Bukkit.getWorld("world_resources");
	}

	private static boolean isLoaded(String name)
	{
		return Bukkit.getWorld(name) != null;
	}
}
