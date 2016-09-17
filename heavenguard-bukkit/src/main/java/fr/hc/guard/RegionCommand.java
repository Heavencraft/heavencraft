package fr.hc.guard;

import java.util.Arrays;

import fr.hc.core.cmd.SubCommandsCommand;
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
import fr.heavencraft.heavenguard.bukkit.commands.owner.AddMemberSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.owner.InfoSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.owner.RemoveMemberSubCommand;

public class RegionCommand extends SubCommandsCommand
{
	public RegionCommand(BukkitHeavenGuard plugin)
	{
		super(plugin, "region", Arrays.asList("rg"));

		addSubCommand("define", new DefineSubCommand());
		addSubCommand("redefine", new RedefineSubCommand());
		addSubCommand("select", new SelectSubCommand());
		addSubCommand("info", new InfoSubCommand());
		addSubCommand("setparent", new SetparentSubCommand());
		addSubCommand("remove", new RemoveSubCommand());
		addSubCommand("flag", new FlagSubCommand());

		addSubCommand("addmember", new AddMemberSubCommand());
		addSubCommand("removemember", new RemoveMemberSubCommand());
		addSubCommand("addowner", new AddOwnerSubCommand());
		addSubCommand("removeowner", new RemoveOwnerSubCommand());

		addSubCommand("loadstate", new LoadStateSubCommand());
		addSubCommand("savestate", new SavestateSubCommand());
	}
}