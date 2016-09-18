package fr.hc.core.cmd.homes;

import java.sql.SQLException;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.BukkitHeavenCore;
import fr.hc.core.HeavenCore;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.db.homes.SetHomeQuery;
import fr.hc.core.db.users.home.UserWithHome;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;

public class SetHomeCommand extends AbstractCommandExecutor
{
	private final HeavenCore plugin;

	public SetHomeCommand(BukkitHeavenCore plugin)
	{
		super(plugin, "sethome");
		this.plugin = plugin;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		int homeNumber = 1;
		if (args.length == 1)
		{
			try
			{
				homeNumber = Integer.parseInt(args[0]);
			}
			catch (final NumberFormatException ex)
			{
				throw new HeavenException("Le nombre {%1$s} est incorrect.", args[0]);
			}
		}

		final Optional<? extends UserWithHome> optUser = plugin.getUserProvider()
				.getUserByUniqueId(player.getUniqueId());
		if (!optUser.isPresent())
			throw new HeavenException("L'UUID n'est pas liée a un compte heavencraft. Contactez un administrateur.");

		final Location loc = player.getLocation();
		try
		{
			new SetHomeQuery(optUser.get(), homeNumber, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(),
					loc.getYaw(), loc.getPitch(), plugin.getHomeProvider()).executeQuery();
		}
		catch (final SQLException e)
		{
			log.error(e.getMessage());
			throw new DatabaseErrorException();
		}
		ChatUtil.sendMessage(player, "Vous avez bien défini votre home {%1$d}", homeNumber);
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