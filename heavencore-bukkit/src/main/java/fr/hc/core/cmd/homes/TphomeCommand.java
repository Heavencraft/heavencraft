package fr.hc.core.cmd.homes;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.BukkitHeavenCore;
import fr.hc.core.CorePermissions;
import fr.hc.core.HeavenCore;
import fr.hc.core.HeavenCoreInstance;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.db.homes.Home;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.HomeNotFoundException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.core.utils.BukkitConversionUtil;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class TphomeCommand extends AbstractCommandExecutor
{
	private final HeavenCore plugin = HeavenCoreInstance.get();

	public TphomeCommand(BukkitHeavenCore plugin)
	{
		super(plugin, "tphome", CorePermissions.TPHOME_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		final int homeNumber;

		switch (args.length)
		{
			case 1: // /tphome <joueur>
				homeNumber = 1;
				break;
			case 2: // /tphome <joueur> <numéro>
				homeNumber = ConversionUtil.toUint(args[1]);
				break;
			default:
				sendUsage(player);
				return;
		}

		final Optional<? extends User> optUser = plugin.getUserProvider()
				.getOptionalUserByName(PlayerUtil.getExactName(args[0]));
		if (!optUser.isPresent())
			throw new UserNotFoundException(args[0]);
		final User user = optUser.get();

		final Optional<Home> optHome = plugin.getHomeProvider().getHomeByUserAndNumber(user, homeNumber);
		if (!optHome.isPresent())
			throw new HomeNotFoundException(user, homeNumber);
		final Home home = optHome.get();

		PlayerUtil.teleportPlayer(player, BukkitConversionUtil.toLocation(home));
		ChatUtil.sendMessage(player, "Vous avez été téléporté au {home %1$s} de {%2$s}.", homeNumber, user);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		notConsoleCommand(sender);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{tphome} <joueur>");
		ChatUtil.sendMessage(sender, "/{tphome} <joueur> <numéro>");
	}
}