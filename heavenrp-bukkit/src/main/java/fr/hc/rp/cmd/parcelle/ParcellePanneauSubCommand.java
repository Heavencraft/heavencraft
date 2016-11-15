package fr.hc.rp.cmd.parcelle;

import java.util.Optional;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.WorldEditUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.HeavenGuard;
import fr.hc.guard.HeavenGuardInstance;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.db.regions.SetParentQuery;
import fr.hc.guard.exceptions.RegionNotFoundException;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.towns.Town;
import fr.hc.rp.db.users.RPUser;

public class ParcellePanneauSubCommand extends SubCommand
{
	private final HeavenRP plugin = HeavenRPInstance.get();
	private final HeavenGuard guard = HeavenGuardInstance.get();

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		int up, down;
		switch (args.length)
		{
			case 2: // /parcelle panneau <ville> <prix>
				up = 20;
				down = 10;
				break;
			case 4: // /parcelle ajouter <ville> <prix> <up> <down>
				up = ConversionUtil.toUint(args[2]);
				down = ConversionUtil.toUint(args[3]);
				break;
			default:
				sendUsage(player);
				return;
		}

		final String townName = args[0];
		final int price = ConversionUtil.toUint(args[1]);

		final Optional<RPUser> optMayor = plugin.getUserProvider().getOptionalUserByUniqueId(player.getUniqueId());
		if (!optMayor.isPresent())
			throw new UnexpectedErrorException();
		final RPUser mayor = optMayor.get();

		final Town town = plugin.getTownProvider().getTownByName(townName);
		if (!town.isMayor(mayor))
			throw new HeavenException("Vous n'êtes pas maire de {%1$s}", town);

		final Optional<Region> optParent = guard.getRegionProvider().getRegionByName(town.getName());
		if (!optParent.isPresent())
			throw new RegionNotFoundException(town.getName());
		final Region parent = optParent.get();

		final Selection selection = WorldEditUtil.getWESelection(player);
		final Location min = selection.getMinimumPoint().add(0, -down, 0);
		final Location max = selection.getMaximumPoint().add(0, up, 0);

		if (!parent.contains(min.getWorld().getName(), min.getBlockX(), min.getBlockY(), min.getBlockZ())
				|| !parent.contains(max.getWorld().getName(), max.getBlockX(), max.getBlockY(), max.getBlockZ()))
			throw new HeavenException("La parcelle sort de la ville.");

		final Block targetBlock = player.getTargetBlock((Set) null, 10);
		if (targetBlock == null)
			throw new HeavenException("Veuillez pointer l'emplacement du panneau.");

		final Block signBlock = targetBlock.getRelative(BlockFace.UP);
		if (signBlock.getType() != Material.AIR)
			throw new HeavenException("Impossible de poser un panneau ici.");

		final String regionName = ParcelleCommandUtil.createRegionName(parent, "parcelle");

		// Create the region
		final Region region = guard.getRegionProvider().createRegion(regionName, selection.getWorld().getName(), //
				min.getBlockX(), min.getBlockY(), min.getBlockZ(), //
				max.getBlockX(), max.getBlockY(), max.getBlockZ());

		// Set parent and add owner
		new SetParentQuery(region, parent, guard.getConnectionProvider())
		{
			@Override
			public void onSuccess()
			{
				signBlock.setType(Material.SIGN_POST);

				final Sign sign = (Sign) signBlock.getState();
				sign.setLine(0, ChatColor.GREEN + "[Parcelle]");
				sign.setLine(1, town.getName());
				sign.setLine(2, Integer.toString(price));
				sign.setLine(3, (max.getBlockX() - min.getBlockX()) + "x" + (max.getBlockZ() - min.getBlockZ()) + "x"
						+ (max.getBlockY() - min.getBlockY()));
				sign.update();

				ChatUtil.sendMessage(player, "La parcelle a été créée avec succès.");
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		// TODO Auto-generated method stub

	}

}
