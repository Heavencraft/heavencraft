package fr.hc.rp.db.quests.server;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.WorldEditUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.worlds.WorldsManager;

public class ServerQuestAdminSetStepSchematicSubCommand extends SubCommand
{
	private final HeavenRP plugin = HeavenRPInstance.get();

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length < 2)
		{
			sendUsage(player);
			return;
		}

		if (player.getWorld() != WorldsManager.getWorld())
		{
			ChatUtil.sendMessage(player, "Cette commande n'est utilisable que sur la carte principale.");
			return;
		}

		final ServerQuestStepProvider provider = plugin.getServerQuestStepProvider();
		final ServerQuestStep step = provider.getById(ConversionUtil.toUint(args[0]));
		final byte[] schematic;

		switch (args[1].toLowerCase())
		{
			case "clipboard":
				if (args.length != 2)
				{
					sendUsage(player);
					return;
				}

				schematic = WorldEditUtil.getPlayerClipboard(player);
				break;

			case "file":
				if (args.length != 3)
				{
					sendUsage(player);
					return;
				}

				schematic = WorldEditUtil.loadSchematicFromFile(player, args[2]);
				break;

			default:
				sendUsage(player);
				return;
		}

		new UpdateServerQuestStepSchematicQuery(step, schematic, provider)
		{

			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, "L'étape %1$s a été mise à jour.", step.getId());
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		notConsoleCommand(sender);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{serverquestadmin setStepSchematic <step id> clipboard");
		ChatUtil.sendMessage(sender, "/{serverquestadmin setStepSchematic <step id> file <file>");
	}
}