package fr.heavencraft.heavenguard.bukkit.commands;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.WorldEditUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.HeavenGuardPermissions;
import fr.hc.guard.db.regions.RedefineQuery;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.exceptions.RegionNotFoundException;

public class RedefineSubCommand extends AbstractSubCommand
{
	public RedefineSubCommand(BukkitHeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.REDEFINE_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		if (args.length != 0)
		{
			sendUsage(sender);
			return;
		}

		final Optional<Region> optRegion = plugin.getRegionProvider().getRegionByName(regionName);
		if (!optRegion.isPresent())
			throw new RegionNotFoundException(regionName);

		final Region region = optRegion.get();

		if (!(sender instanceof Player))
			throw new HeavenException("Il faut être un joueur pour utiliser /region redefine.");

		final Selection selection = WorldEditUtil.getWESelection((Player) sender);
		final Location min = selection.getMinimumPoint();
		final Location max = selection.getMaximumPoint();

		new RedefineQuery(region, selection.getWorld().getName(), min.getBlockX(), min.getBlockY(), min.getBlockZ(), //
				max.getBlockX(), max.getBlockY(), max.getBlockZ(), plugin.getConnectionProvider())
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(sender, "La protection {%1$s} a bien été redéfinie.", region);
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(sender, ex.getMessage());
			}
		}.schedule();
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {redefine} <protection>");
	}
}