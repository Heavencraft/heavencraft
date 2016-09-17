package fr.heavencraft.heavenguard.bukkit.commands;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import org.bukkit.command.CommandSender;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.DateUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.HeavenGuardPermissions;
import fr.hc.guard.cmd.HeavenGuardSubCommand;
import fr.hc.guard.db.Flag;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.exceptions.RegionNotFoundException;

public class FlagSubCommand extends HeavenGuardSubCommand
{
	public FlagSubCommand()
	{
		super(HeavenGuardPermissions.FLAG_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		switch (args.length)
		{
			case 1: // Remove the flag from the region.
				flag(sender, regionName, args[0], null);
				break;

			case 2: // Set the flag's value.
				flag(sender, regionName, args[0], args[1]);
				break;

			default:
				sendUsage(sender);
				break;
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {flag} <protection> <flag> : pour supprimer un flag");
		ChatUtil.sendMessage(sender, "/rg {flag} <protection> <flag> <valeur> : pour ajouter un flag");
	}

	private void flag(CommandSender sender, String regionName, String flagName, String value) throws HeavenException
	{
		final Flag flag = Flag.getUniqueInstanceByName(flagName);

		if (flag == null)
			throw new HeavenException("Le flag {%1$s} n'existe pas.", flagName);

		final Optional<Region> optRegion = plugin.getRegionProvider().getRegionByName(regionName);
		if (optRegion.isPresent())
			throw new RegionNotFoundException(regionName);
		final Region region = optRegion.get();

		switch (flag.getType())
		{
			case BOOLEAN:
				region.getFlagHandler().setBooleanFlag(flag, value != null ? Boolean.parseBoolean(value) : null);
				break;

			case TIMESTAMP:
				if (value == null)
					region.getFlagHandler().setTimestampFlag(flag, null);

				else
				{
					final Date date = DateUtil.parseDateTime(value);
					region.getFlagHandler().setTimestampFlag(flag, new Timestamp(date.getTime()));
				}
				break;

			case BYTE_ARRAY:
				throw new HeavenException("Ce type de flag ne peut être changé via /rg flag.");
		}

		ChatUtil.sendMessage(sender, "La protection {%1$s} a désormais : {%2$s} = {%3$s}", region.getName(),
				flag.getName(), value);
	}
}