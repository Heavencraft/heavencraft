package fr.heavencraft.heavenguard.bukkit.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.core.utils.WorldEditUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.HeavenGuardPermissions;
import fr.hc.guard.db.regions.AddMemberQuery;
import fr.hc.guard.db.regions.Region;

public class DefineSubCommand extends AbstractSubCommand
{
	public DefineSubCommand(BukkitHeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.DEFINE_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Selection selection = WorldEditUtil.getWESelection((Player) sender);
		final Collection<User> users = new ArrayList<User>();

		if (args.length != 0)
		{
			for (final String arg : args)
			{
				final Optional<? extends User> optUser = plugin.getUserProvider().getUserByName(arg);
				if (!optUser.isPresent())
					throw new UserNotFoundException(arg);
				users.add(optUser.get());
			}
		}

		define(sender, regionName, selection, users);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {define} <protection>");
	}

	private void define(CommandSender sender, String name, Selection selection, Collection<User> users)
			throws HeavenException
	{
		final Location min = selection.getMinimumPoint();
		final Location max = selection.getMaximumPoint();

		// Create the region
		final Region region = plugin.getRegionProvider().createRegion(name, selection.getWorld().getName(), //
				min.getBlockX(), min.getBlockY(), min.getBlockZ(), //
				max.getBlockX(), max.getBlockY(), max.getBlockZ());

		ChatUtil.sendMessage(sender, "La région {%1$s} a bien été créée.", name);

		// Add the initial owners
		if (!users.isEmpty())
		{
			for (final User user : users)
			{
				new AddMemberQuery(region, user, true, plugin.getConnectionProvider())
				{
					@Override
					public void onSuccess()
					{
						ChatUtil.sendMessage(sender, "{%1$s} est maintenant propriétaire de la protection {%2$s}.",
								user, region);
					}
				}.schedule();
			}
		}
	}
}