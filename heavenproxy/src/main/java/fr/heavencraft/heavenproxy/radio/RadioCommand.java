package fr.heavencraft.heavenproxy.radio;

import java.util.LinkedList;
import java.util.Queue;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.commands.HeavenCommand;
import fr.hc.core.exceptions.HeavenException;

public class RadioCommand extends HeavenCommand
{
	// Messages
	private static final String RADIO_ENABLED = "§e[Radio]§f Les dédicaces viennent d'être activées.";
	private static final String RADIO_DISABLED = "§e[Radio]§f Les dédicaces viennent d'être désactivées.";

	private static final String RADIO_PERMISSION = "heavencraft.commands.radio";

	private boolean enabled = false;
	private final Queue<String> dedications = new LinkedList<String>();

	public RadioCommand()
	{
		super("radio", "", new String[] {});
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (sender.hasPermission(RADIO_PERMISSION))
			onRadioHostCommand(sender, args);
		else
			onPlayerCommand(sender, args);
	}

	private void onPlayerCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			Utils.sendMessage(sender, "/{radio} <dédicace>");
			return;
		}

		if (!enabled)
			throw new HeavenException("Le système de dédicaces est désactivé.");

		dedications.add("{" + sender.getName() + "} : " + Utils.ArrayToString(args, 0, " "));
		Utils.sendMessage(sender, "Vous dédicace a été transmise aux animateurs.");

		sendToRadioHost(ChatColor.YELLOW + "Une dédicace vient d'arriver !");
	}

	private void onRadioHostCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			Utils.sendMessage(sender, "/{radio} <on|off> : activer/désactiver les dédicaces");
			Utils.sendMessage(sender, "/{radio} dedi : prendre une dédicace");
			return;
		}

		switch (args[0])
		{
			case "off":
				enabled = false;
				Utils.broadcastMessage(RADIO_DISABLED);
				break;

			case "on":
				enabled = true;
				Utils.broadcastMessage(RADIO_ENABLED);
				break;

			case "dedi":
				final String dedication = dedications.poll();

				if (dedication == null)
					Utils.sendMessage(sender, "Aucune dédicace à prendre.");
				else
					Utils.sendMessage(sender, dedication);
				break;

			default:
				Utils.sendMessage(sender, "/{radio} <on|off> : activer/désactiver les dédicaces");
				Utils.sendMessage(sender, "/{radio} dedi : prendre une dédicace");
				break;
		}
	}

	private static void sendToRadioHost(String message)
	{
		for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
			if (player.hasPermission(RADIO_PERMISSION))
				player.sendMessage(TextComponent.fromLegacyText(message));
	}
}