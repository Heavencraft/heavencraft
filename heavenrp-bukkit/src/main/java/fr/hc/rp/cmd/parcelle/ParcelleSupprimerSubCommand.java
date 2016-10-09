package fr.hc.rp.cmd.parcelle;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.HeavenGuard;
import fr.hc.guard.HeavenGuardInstance;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.exceptions.RegionNotFoundException;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.towns.Town;
import fr.hc.rp.db.users.RPUser;

public class ParcelleSupprimerSubCommand extends SubCommand
{
	private final HeavenRP plugin = HeavenRPInstance.get();
	private final HeavenGuard guard = HeavenGuardInstance.get();

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 2)
		{
			sendUsage(player);
			return;
		}

		final String townName = args[0];
		final String regionName = args[1];

		final Optional<RPUser> optMayor = plugin.getUserProvider().getOptionalUserByUniqueId(player.getUniqueId());
		if (!optMayor.isPresent())
			throw new UnexpectedErrorException();
		final RPUser mayor = optMayor.get();

		final Town town = plugin.getTownProvider().getTownByName(townName);
		if (!town.isMayor(mayor))
			throw new HeavenException("Vous n'êtes pas maire de {%1$s}", town);

		final Optional<Region> optRegion = guard.getRegionProvider().getRegionByName(regionName);
		if (!optRegion.isPresent())
			throw new RegionNotFoundException(regionName);
		final Region region = optRegion.get();

		final Optional<Region> optParent = region.getParent();
		if (!optParent.isPresent() || !optParent.get().getName().equals(town.getName()))
			throw new HeavenException("Cette protection ne fait pas partie de votre ville.");

		guard.getRegionProvider().deleteRegion(region.getName());
		ChatUtil.sendMessage(player, "La parcelle a été supprimée avec succès.");
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		notConsoleCommand(sender);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{parcelle} supprimer <nom de la ville> <nom de la parcelle>");
	}
}