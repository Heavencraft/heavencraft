package fr.hc.rp.npc;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.RPPermissions;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class NPCAdminCommand extends AbstractCommandExecutor
{
	public NPCAdminCommand(BukkitHeavenRP plugin)
	{
		super(plugin, "npcadmin", RPPermissions.NPCADMIN_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(player);
			return;
		}

		switch (args[0].toLowerCase())
		{
			case "setnpctag":
				setNPCTag(player, args);
				break;

			default:
				sendUsage(player);
				break;
		}
	}

	private void setNPCTag(Player player, String[] args) throws HeavenException
	{
		String npcTag;

		switch (args.length)
		{
			case 1:
				npcTag = null;
				break;

			case 2:
				npcTag = args[1];
				break;

			default:
				sendUsage(player);
				return;
		}

		final List<MetadataValue> metadata = player.getMetadata("selected");
		if (metadata.isEmpty())
			throw new HeavenException("Vous n'avez selectionné aucun NPC.");

		final NPC npc = CitizensAPI.getNPCRegistry().getByUniqueIdGlobal((UUID) metadata.get(0).value());
		if (npc == null)
			throw new HeavenException("Vous n'avez selectionné aucun NPC.");

		if (npcTag != null)
		{
			npc.data().setPersistent(NPCMessageListener.NPC_TAG, npcTag);
			ChatUtil.sendMessage(player, "Le npc {%1$s} a désormais le tag {%2$s}.", npc.getName(), npcTag);
		}
		else
		{
			npc.data().remove(NPCMessageListener.NPC_TAG);
			ChatUtil.sendMessage(player, "Le npc {%1$s} n'a désormais plus de tag.", npc.getName(), npcTag);
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		notConsoleCommand(sender);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{npcadmin} setnpctag <tag>");
	}
}