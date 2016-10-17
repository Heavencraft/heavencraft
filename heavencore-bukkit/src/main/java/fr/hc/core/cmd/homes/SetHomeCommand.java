package fr.hc.core.cmd.homes;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.HeavenCore;
import fr.hc.core.HeavenCoreInstance;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.db.homes.SetHomeQuery;
import fr.hc.core.db.users.home.UserWithHome;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class SetHomeCommand extends AbstractCommandExecutor
{
	private final HeavenCore plugin = HeavenCoreInstance.get();

	public SetHomeCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "sethome");
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

		final Optional<? extends UserWithHome> optUser = plugin.getUserProvider()
				.getOptionalUserByUniqueId(player.getUniqueId());
		if (!optUser.isPresent())
			throw new UnexpectedErrorException();

		final UserWithHome user = optUser.get();
		final Location loc = player.getLocation();

		new SetHomeQuery(user, homeNumber, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),
				loc.getPitch(), plugin.getHomeProvider())
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, "Vous avez bien défini votre home {%1$d}", homeNumber);
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		notConsoleCommand(sender);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{sethome} <numéro du home>");
	}
}