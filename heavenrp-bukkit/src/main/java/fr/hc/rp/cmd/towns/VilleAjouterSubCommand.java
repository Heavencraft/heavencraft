package fr.hc.rp.cmd.towns;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.RPPermissions;
import fr.hc.rp.db.towns.AddMayorQuery;
import fr.hc.rp.db.towns.Town;
import fr.hc.rp.db.towns.TownProvider;
import fr.hc.rp.db.users.RPUser;
import fr.hc.rp.db.users.RPUserProvider;

public class VilleAjouterSubCommand extends SubCommand
{
	private static final String SUCCESS = "{%1$s} est désormais maire de {%2$s}.";

	private final HeavenRP plugin = HeavenRPInstance.get();

	public VilleAjouterSubCommand()
	{
		super(RPPermissions.ADD_MAYOR);
	}

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length < 2)
		{
			sendUsage(sender);
			return;
		}

		final String townName = args[0];

		final RPUserProvider userProvider = plugin.getUserProvider();
		final TownProvider townProvider = plugin.getTownProvider();

		final Town town = HeavenRPInstance.get().getTownProvider().getTownByName(townName);

		for (int i = 1; i != args.length; i++)
		{
			final Optional<RPUser> optUser = userProvider.getOptionalUserByName(PlayerUtil.getExactName(args[i]));
			if (!optUser.isPresent())
			{
				ChatUtil.sendMessage(sender, UserNotFoundException.MESSAGE, args[i]);
				continue;
			}

			final User user = optUser.get();

			new AddMayorQuery(town, user, townProvider)
			{
				@Override
				public void onSuccess()
				{
					ChatUtil.sendMessage(sender, SUCCESS, user, town);
				}

				@Override
				public void onException(HeavenException ex)
				{
					ChatUtil.sendMessage(sender, ex.getMessage());
				}
			}.schedule();
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{ville} ajouter <ville> <nom du maire>");
	}
}