package fr.heavencraft.heavenproxy.commands;

import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class NexusCommand extends HeavenCommand
{
	public NexusCommand()
	{
		super("nexus", null, new String[] { "hub", "lobby" });
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (!(sender instanceof ProxiedPlayer))
			return;

		final ProxiedPlayer player = (ProxiedPlayer) sender;

		player.connect(_plugin.getProxy().getServerInfo("semirp"));
		Utils.sendMessage(player, "Vous avez été téléporté au Nexus.");
	}
}