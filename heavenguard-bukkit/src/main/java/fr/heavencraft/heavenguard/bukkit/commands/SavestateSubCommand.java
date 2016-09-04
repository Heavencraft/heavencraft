package fr.heavencraft.heavenguard.bukkit.commands;

import java.util.Optional;

import org.bukkit.command.CommandSender;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.HeavenGuardPermissions;
import fr.hc.guard.RegionUtil;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.exceptions.RegionNotFoundException;

public class SavestateSubCommand extends AbstractSubCommand
{
	public SavestateSubCommand(BukkitHeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.SAVESTATE_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Optional<Region> optRegion = plugin.getRegionProvider().getRegionByName(regionName);
		if (optRegion.isPresent())
			throw new RegionNotFoundException(regionName);
		final Region region = optRegion.get();

		ChatUtil.sendMessage(sender, "Sauvegarde de la protection {%1$s}...", region.getName());
		RegionUtil.loadState(region);
		ChatUtil.sendMessage(sender, "La protection {%1$s} a bien été sauvegardée.", region.getName());
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {savestate} <protection>");
	}
}