package fr.hc.rp.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class RejoindreCommand extends AbstractCommandExecutor
{
	private static Map<String, String> requests = new HashMap<String, String>();

	public RejoindreCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "rejoindre");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(player);
			return;
		}

		final Player destination = PlayerUtil.getPlayer(args[0]);
		if ((player.getWorld().toString() != "world_resources") || player.getWorld() != destination.getWorld())
			throw new HeavenException("Vous devez être tous les deux en map ressource pour utiliser cette commande.");

		ChatUtil.sendMessage(destination, "{%1$s} souhaite vous rejoindre. Tapez /accepter {%1$s} pour accepter.",
				player.getName());

		ChatUtil.sendMessage(player, "Votre demande de téléportation a été transmise à {%1$s}", destination.getName());
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{rejoindre} <joueur>");
		ChatUtil.sendMessage(sender, "/{accepter} <joueur>");
	}

	public static void addRequest(String toTeleport, String destination)
	{
		requests.put(toTeleport, destination);
	}

	public static boolean acceptRequest(String toTeleport, String destination)
	{
		final String destination2 = requests.get(toTeleport);

		if (destination.equalsIgnoreCase(destination2))
		{
			requests.remove(toTeleport);
			return true;
		}

		return false;
	}
}
