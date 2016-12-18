package fr.hc.rp.db.quests.server;

import fr.hc.core.cmd.SubCommandsCommand;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.RPPermissions;

// Internal command executed by NPC to handle server quests action
public class ServerQuestCommand extends SubCommandsCommand
{
	public ServerQuestCommand(BukkitHeavenRP plugin, ServerQuestInventoryListener serverQuestInventoryListener)
	{
		super(plugin, "serverquest", RPPermissions.SERVERQUEST_COMMAND);

		addSubCommand("inventory", new ServerQuestInventorySubCommand(serverQuestInventoryListener));
	}
}