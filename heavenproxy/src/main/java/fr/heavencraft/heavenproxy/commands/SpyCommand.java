package fr.heavencraft.heavenproxy.commands;

import net.md_5.bungee.api.CommandSender;
import fr.hc.core.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.listeners.SpyListener;

public class SpyCommand extends HeavenCommand {

	public SpyCommand()
	{
		super("spy", "heavencraft.commands.spy", new String[] {});
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException {
		SpyListener.toggleSpy(sender.getName());

	}

}
