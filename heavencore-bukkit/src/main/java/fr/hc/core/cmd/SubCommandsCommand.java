package fr.hc.core.cmd;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.exceptions.HeavenException;

// TODO: Change name
public class SubCommandsCommand extends AbstractCommandExecutor
{
	private final Collection<SubCommand> subCommands = new HashSet<SubCommand>();
	private final Map<String, SubCommand> subCommandsByName = new HashMap<String, SubCommand>();

	public SubCommandsCommand(AbstractBukkitPlugin plugin, String name)
	{
		super(plugin, name);
	}

	public SubCommandsCommand(AbstractBukkitPlugin plugin, String name, List<String> aliases)
	{
		super(plugin, name, aliases);
	}

	public void addSubCommand(String name, SubCommand subCommand)
	{
		subCommands.add(subCommand);
		subCommandsByName.put(name.toLowerCase(), subCommand);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(player);
			return;
		}

		final SubCommand subCommand = subCommandsByName.get(args[0].toLowerCase());
		if (subCommand == null)
		{
			sendUsage(player);
			return;
		}

		final String[] args2 = Arrays.copyOfRange(args, 1, args.length);
		if (!subCommand.canExecute(player, args2))
		{
			sendUsage(player);
			return;
		}

		subCommand.onPlayerCommand(player, args2);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(sender);
			return;
		}

		final SubCommand subCommand = subCommandsByName.get(args[0].toLowerCase());
		if (subCommand == null)
		{
			sendUsage(sender);
			return;
		}

		final String[] args2 = Arrays.copyOfRange(args, 1, args.length);
		if (!subCommand.canExecute(sender, args2))
		{
			sendUsage(sender);
			return;
		}

		subCommand.onConsoleCommand(sender, args2);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		for (final SubCommand subCommand : subCommands)
			if (subCommand.canSeeUsage(sender))
				subCommand.sendUsage(sender);
	}
}