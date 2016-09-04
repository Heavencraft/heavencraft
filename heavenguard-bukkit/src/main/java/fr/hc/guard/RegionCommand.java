package fr.hc.guard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.heavencraft.heavenguard.bukkit.commands.AddOwnerSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.DefineSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.FlagSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.LoadStateSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.RedefineSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.RemoveOwnerSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.RemoveSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.SavestateSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.SelectSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.SetparentSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.SubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.owner.AddMemberSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.owner.InfoSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.owner.RemoveMemberSubCommand;

public class RegionCommand extends AbstractCommandExecutor
{
	private final Map<String, SubCommand> subCommands = new HashMap<String, SubCommand>();

	public RegionCommand(BukkitHeavenGuard plugin)
	{
		super(plugin, "region", Arrays.asList("rg"));

		subCommands.put("define", new DefineSubCommand(plugin));
		subCommands.put("redefine", new RedefineSubCommand(plugin));
		subCommands.put("select", new SelectSubCommand(plugin));
		subCommands.put("info", new InfoSubCommand(plugin));
		subCommands.put("setparent", new SetparentSubCommand(plugin));
		subCommands.put("remove", new RemoveSubCommand(plugin));
		subCommands.put("flag", new FlagSubCommand(plugin));

		subCommands.put("addmember", new AddMemberSubCommand(plugin));
		subCommands.put("removemember", new RemoveMemberSubCommand(plugin));
		subCommands.put("addowner", new AddOwnerSubCommand(plugin));
		subCommands.put("removeowner", new RemoveOwnerSubCommand(plugin));

		subCommands.put("loadstate", new LoadStateSubCommand(plugin));
		subCommands.put("savestate", new SavestateSubCommand(plugin));
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length < 2)
		{
			sendUsage(sender);
			return;
		}

		final String command = args[0].toLowerCase();
		final String regionName = args[1].toLowerCase();

		final String[] args2 = new String[args.length - 2];

		if (args2.length != 0)
		{
			for (int i = 0; i != args2.length; i++)
				args2[i] = args[i + 2].toLowerCase();
		}

		final SubCommand subCommand = subCommands.get(command);

		if (subCommand == null)
		{
			sendUsage(sender);
			return;
		}

		if (!subCommand.canExecute(sender, regionName))
			return;

		subCommand.execute(sender, regionName, args2);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		for (final SubCommand subCommand : subCommands.values())
			if (subCommand.canExecute(sender, null))
				subCommand.sendUsage(sender);
	}
}