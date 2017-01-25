package fr.hc.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
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
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.file.FilenameException;
import com.sk89q.worldedit.world.registry.WorldData;

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

	public static byte[] getPlayerClipboard(Player player) throws HeavenException
	{
		try
		{
			final ClipboardHolder clipboardHolder = getWorldEdit().getSession(player).getClipboard();
			return clipboardToSchematic(clipboardHolder.getClipboard(), clipboardHolder.getWorldData());
		}
		catch (final EmptyClipboardException ex)
		{
			throw new HeavenException("Votre clipboard WorldEdit est vide.");
		}
	}

	public static byte[] save(World world, Location pos1, Location pos2) throws HeavenException
	{
		final com.sk89q.worldedit.world.World weWorld = toWorldEdit(world);

		final Region region = new CuboidRegion(weWorld, toWorldEdit(pos1), toWorldEdit(pos2));
		final BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

		try
		{
			Operations.complete(new ForwardExtentCopy(weWorld, region, clipboard, clipboard.getOrigin()));
		}
		catch (final WorldEditException ex)
		{
			ex.printStackTrace();
			throw new HeavenException(ex.getMessage());
		}

		return clipboardToSchematic(clipboard, weWorld.getWorldData());
	}

	public static void load(byte[] schematic, World world, Location pos1, Location pos2) throws HeavenException
	{
		final com.sk89q.worldedit.world.World weWorld = toWorldEdit(world);
		final Region region = new CuboidRegion(weWorld, toWorldEdit(pos1), toWorldEdit(pos2));

		final Clipboard clipboard = schematicToClipboard(schematic, weWorld.getWorldData());
		paste(clipboard, weWorld, region.getMinimumPoint());
	}

	public static void pasteAtOrigin(byte[] schematic, World world) throws HeavenException
	{
		final com.sk89q.worldedit.world.World weWorld = toWorldEdit(world);

		final Clipboard clipboard = schematicToClipboard(schematic, weWorld.getWorldData());
		paste(clipboard, weWorld, clipboard.getOrigin());
	}

	public static byte[] loadSchematicFromFile(Player player, String filename) throws HeavenException
	{
		final WorldEditPlugin worldEditPlugin = getWorldEdit();
		final WorldEdit worldEdit = worldEditPlugin.getWorldEdit();

		final LocalConfiguration config = worldEdit.getConfiguration();

		final File dir = worldEdit.getWorkingDirectoryFile(config.saveDir);

		try
		{
			final File f = worldEdit.getSafeOpenFile(worldEditPlugin.wrapPlayer(player), dir, filename, "schematic", "schematic");
			if (!f.exists())
				throw new HeavenException("Le fichier %1$s.schematic n'existe pas.", filename);

			return Files.readAllBytes(f.toPath());
		}
		catch (final FilenameException | IOException ex)
		{
			ex.printStackTrace();
			throw new HeavenException(ex.getMessage());
		}
	}

	private static Clipboard schematicToClipboard(byte[] schematic, WorldData data) throws HeavenException
	{
		try (ByteArrayInputStream bais = new ByteArrayInputStream(schematic))
		{
			final ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(bais);
			return reader.read(data);
		}
		catch (final IOException ex)
		{
			ex.printStackTrace();
			throw new HeavenException(ex.getMessage());
		}
	}

	private static byte[] clipboardToSchematic(Clipboard clipboard, WorldData data) throws HeavenException
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(baos))
		{
			writer.write(clipboard, data);
			return baos.toByteArray();
		}
		catch (final IOException ex)
		{
			ex.printStackTrace();
			throw new HeavenException(ex.getMessage());
		}
	}

	private static void paste(Clipboard clipboard, com.sk89q.worldedit.world.World world, Vector to) throws HeavenException
	{
		try
		{
			final ForwardExtentCopy copy = new ForwardExtentCopy(clipboard, clipboard.getRegion(), world, to);
			Operations.complete(copy);
		}
		catch (final WorldEditException ex)
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