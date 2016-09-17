package fr.hc.core.cmd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.exceptions.HeavenException;

// TODO: Change name
public class SubCommandsCommand extends AbstractCommandExecutor
{
	private final Map<String, SubCommand> subCommands = new HashMap<String, SubCommand>();

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
		subCommands.put(name, subCommand);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(player);
			return;
		}

		final SubCommand subCommand = subCommands.get(args[0].toLowerCase());
		if (subCommand == null || !subCommand.canExecute(player))
		{
			sendUsage(player);
			return;
		}

		subCommand.onPlayerCommand(player, Arrays.copyOfRange(args, 1, args.length));
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(sender);
			return;
		}

		final SubCommand subCommand = subCommands.get(args[0].toLowerCase());
		if (subCommand == null || !subCommand.canExecute(sender))
		{
			sendUsage(sender);
			return;
		}

		subCommand.onConsoleCommand(sender, Arrays.copyOfRange(args, 1, args.length));
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		for (final SubCommand subCommand : subCommands.values())
			if (subCommand.canExecute(sender))
				subCommand.sendUsage(sender);
	}
}