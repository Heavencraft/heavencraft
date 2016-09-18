package fr.hc.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;

import fr.hc.core.exceptions.HeavenException;

public class WorldEditUtil
{
	public static WorldEditPlugin getWorldEdit()
	{
		return (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	}

	public static Selection getWESelection(Player player) throws HeavenException
	{
		final Selection selection = getWorldEdit().getSelection(player);

		if (selection == null)
			throw new HeavenException("Vous devez sélectionner une zone avec le hâche.");

		return selection;
	}

	public static byte[] save(World world, Location pos1, Location pos2) throws HeavenException
	{
		final com.sk89q.worldedit.world.World weWorld = toWorldEdit(world);

		final Region region = new CuboidRegion(weWorld, toWorldEdit(pos1), toWorldEdit(pos2));
		final BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

		try
		{
			Operations.complete(new ForwardExtentCopy(weWorld, region, clipboard, clipboard.getOrigin()));

			try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
			{
				final ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(baos);
				writer.write(clipboard, weWorld.getWorldData());
				writer.close();

				return baos.toByteArray();
			}
		}
		catch (WorldEditException | IOException ex)
		{
			ex.printStackTrace();
			throw new HeavenException(ex.getMessage());
		}

	}

	public static void load(byte[] schematic, World world, Location pos1, Location pos2) throws HeavenException
	{
		final com.sk89q.worldedit.world.World weWorld = toWorldEdit(world);
		final Region region = new CuboidRegion(weWorld, toWorldEdit(pos1), toWorldEdit(pos2));

		try (ByteArrayInputStream bais = new ByteArrayInputStream(schematic))
		{
			final ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(bais);
			final Clipboard clipboard = reader.read(weWorld.getWorldData());

			final ForwardExtentCopy copy = new ForwardExtentCopy(clipboard, clipboard.getRegion(), weWorld,
					region.getMinimumPoint());
			Operations.complete(copy);

		}
		catch (WorldEditException | IOException ex)
		{
			ex.printStackTrace();
			throw new HeavenException(ex.getMessage());
		}

	}

	private static com.sk89q.worldedit.world.World toWorldEdit(World world)
	{
		return new BukkitWorld(world);
	}

	private static Vector toWorldEdit(Location location)
	{
		return BukkitUtil.toVector(location);
	}
}