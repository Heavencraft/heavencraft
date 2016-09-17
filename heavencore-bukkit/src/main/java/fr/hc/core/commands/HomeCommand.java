package fr.hc.core.commands;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.BukkitHeavenCore;
import fr.hc.core.HeavenCore;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.db.homes.Home;
import fr.hc.core.db.users.User;
import fr.hc.core.db.users.home.UserWithHome;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class HomeCommand extends AbstractCommandExecutor
{

	private final HeavenCore core;

	public HomeCommand(BukkitHeavenCore plugin)
	{
		super(plugin, "home");
		this.core = plugin;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		int number;

		switch (args.length)
		{
			case 0:
				number = 1;
				break;
			case 1:
				try
				{
					number = Integer.parseInt(args[0]);
				}
				catch (final NumberFormatException ex)
				{
					throw new HeavenException("Le nombre {%1$s} est incorrect.", args[0]);
				}
				break;
			default:
				sendUsage(player);
				return;
		}

		final Optional<? extends UserWithHome> optUser = core.getUserProvider().getUserByUniqueId(player.getUniqueId());
		final User user = optUser.get();
		final Optional<Home> optHome = core.getHomeProvider().getHomeByUserAndNumber(user, number);

		if (!optHome.isPresent())
			throw new HeavenException("Vous ne possédez pas le home demandé. Faites {/buyhome} pour l'acquérir");

		Location location = new Location(Bukkit.getWorld(optHome.get().getWorld()), optHome.get().getX(),
				optHome.get().getY(), optHome.get().getZ(), optHome.get().getYaw(), optHome.get().getPitch());

		PlayerUtil.teleportPlayer(player, location);
		ChatUtil.sendMessage(player, "Téléportation au home {%1$s} effectuée!", number);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		// TODO Auto-generated method stub

	}

}
