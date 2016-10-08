package fr.hc.core.utils;

import org.bukkit.Material;

public class BukkitUtil
{
	public static boolean isChest(Material type)
	{
		switch (type)
		{
			case CHEST:
			case TRAPPED_CHEST:
				return true;
			default:
				return false;
		}
	}
}