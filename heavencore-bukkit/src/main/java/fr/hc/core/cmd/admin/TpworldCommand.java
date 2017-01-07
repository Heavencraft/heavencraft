package fr.hc.core.cmd.admin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.CorePermissions;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;

public class TpworldCommand extends AbstractCommandExecutor
{
	public TpworldCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "tpworld", CorePermissions.TPWORLD_COMMAND);
	}

	@Override
	protected void onPlayerCommand(final Player player, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(player);
			return;
		}

		final World world = Bukkit.getWorld(args[0]);

		if (world == null)
			throw new HeavenException("Le monde {%1$s} n'existe pas.", args[0]);

		Location location = player.getLocation();
		location.setWorld(world);

		player.teleport(location);

		ChatUtil.sendMessage(player, "Vous avez été téléporté dans le monde {%1$s}.", world.getName());
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{tpworld} <monde>");
	}
}
