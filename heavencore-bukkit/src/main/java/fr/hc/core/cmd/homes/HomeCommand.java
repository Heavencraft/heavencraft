package fr.hc.core.cmd.homes;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.HeavenCore;
import fr.hc.core.HeavenCoreInstance;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.db.homes.Home;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.HomeNotFoundException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.utils.BukkitConversionUtil;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class HomeCommand extends AbstractCommandExecutor
{
	private final HeavenCore plugin = HeavenCoreInstance.get();

	public HomeCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "home");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		final int homeNumber;

		switch (args.length)
		{
			case 0:
				homeNumber = 1;
				break;

			case 1:
				homeNumber = ConversionUtil.toUint(args[0]);
				break;

			default:
				sendUsage(player);
				return;
		}

		final Optional<? extends User> optUser = plugin.getUserProvider()
				.getOptionalUserByUniqueId(player.getUniqueId());
		if (!optUser.isPresent())
			throw new UnexpectedErrorException();

		final User user = optUser.get();

		final Optional<Home> optHome = plugin.getHomeProvider().getHomeByUserAndNumber(user, homeNumber);
		if (!optHome.isPresent())
			throw new HomeNotFoundException(homeNumber);

		PlayerUtil.teleportPlayer(player, BukkitConversionUtil.toLocation(optHome.get()));
		ChatUtil.sendMessage(player, "Téléportation au home {%1$s} effectuée!", homeNumber);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		notConsoleCommand(sender);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{home} <numéro du home>");
	}
}