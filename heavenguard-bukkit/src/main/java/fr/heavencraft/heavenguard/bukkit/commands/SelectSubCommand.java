package fr.heavencraft.heavenguard.bukkit.commands;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.WorldEditUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.HeavenGuardPermissions;
import fr.hc.guard.cmd.HeavenGuardSubCommand;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.exceptions.RegionNotFoundException;

public class SelectSubCommand extends HeavenGuardSubCommand
{
	public SelectSubCommand()
	{
		super(HeavenGuardPermissions.SELECT_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		if (args.length != 0)
		{
			sendUsage(sender);
			return;
		}

		if (!(sender instanceof Player))
			throw new HeavenException("Il faut être un joueur pour utiliser /region select.");

		final Optional<Region> optRegion = plugin.getRegionProvider().getRegionByName(regionName);
		if (!optRegion.isPresent())
			throw new RegionNotFoundException(regionName);

		final Region region = optRegion.get();

		final World world = Bukkit.getWorld(region.getWorld());

		final Selection selection = new CuboidSelection(world, //
				new Location(world, region.getMinX(), region.getMinY(), region.getMinZ()), //
				new Location(world, region.getMaxX(), region.getMaxY(), region.getMaxZ()));

		WorldEditUtil.getWorldEdit().setSelection((Player) sender, selection);
		ChatUtil.sendMessage(sender, "La protection {%1$s} a (peut-être) été selectionnée.", regionName);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {select} <protection>");
	}
}