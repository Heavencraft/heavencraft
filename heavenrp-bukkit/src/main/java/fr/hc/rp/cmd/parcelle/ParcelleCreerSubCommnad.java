package fr.hc.rp.cmd.parcelle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.core.tasks.queries.BatchQuery;
import fr.hc.core.tasks.queries.Query;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.WorldEditUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.HeavenGuard;
import fr.hc.guard.HeavenGuardInstance;
import fr.hc.guard.db.regions.AddMemberQuery;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.db.regions.SetParentQuery;
import fr.hc.guard.exceptions.RegionNotFoundException;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.towns.Town;
import fr.hc.rp.db.users.RPUser;

public class ParcelleCreerSubCommnad extends SubCommand
{
	private final HeavenRP plugin = HeavenRPInstance.get();
	private final HeavenGuard guard = HeavenGuardInstance.get();

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		int up, down;
		switch (args.length)
		{
			case 2: // /parcelle ajouter <ville> <joueur>
				up = 20;
				down = 10;
				break;
			case 4: // /parcelle ajouter <ville> <joueur> <up> <down>
				up = ConversionUtil.toUint(args[2]);
				down = ConversionUtil.toUint(args[3]);
				break;
			default:
				sendUsage(player);
				return;
		}

		final String townName = args[0];
		final String playerName = args[1];

		final Optional<RPUser> optMayor = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		if (!optMayor.isPresent())
			throw new UnexpectedErrorException();
		final RPUser mayor = optMayor.get();

		final Town town = plugin.getTownProvider().getTownByName(townName);
		if (!town.isMayor(mayor))
			throw new HeavenException("Vous n'êtes pas maire de {%1$s}", town);

		final Optional<RPUser> optUser = plugin.getUserProvider().getUserByName(PlayerUtil.getExactName(playerName));
		if (!optUser.isPresent())
			throw new UserNotFoundException(playerName);
		final RPUser user = optUser.get();

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

		final String regionName = createRegionName(parent, user);

		// Create the region
		final Region region = guard.getRegionProvider().createRegion(regionName, selection.getWorld().getName(), //
				min.getBlockX(), min.getBlockY(), min.getBlockZ(), //
				max.getBlockX(), max.getBlockY(), max.getBlockZ());

		// Set parent and add owner
		final List<Query> queries = new ArrayList<Query>();
		queries.add(new SetParentQuery(region, parent, guard.getConnectionProvider()));
		queries.add(new AddMemberQuery(region, user, true, guard.getConnectionProvider()));
		new BatchQuery(queries)
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, "La parcelle a été créée avec succès.");
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();
	}

	private String createRegionName(Region parent, User user)
	{
		String regionName;
		int i = 1;
		do
		{
			regionName = parent.getName() + "_" + user.getName() + "_" + i++;
		}
		while (guard.getRegionProvider().regionExists(regionName));

		return regionName;
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		notConsoleCommand(sender);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{parcelle} créer <nom de la ville> <nom du joueur>");
		ChatUtil.sendMessage(sender, "/{parcelle} créer <nom de la ville> <nom du joueur> <up> <down>");
	}
}