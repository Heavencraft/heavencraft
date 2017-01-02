package fr.hc.rp.db.quests.server;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.cmd.SubCommandsCommand;
import fr.hc.rp.RPPermissions;

public class ServerQuestAdminCommand extends SubCommandsCommand
{
	public ServerQuestAdminCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "serverquestadmin", RPPermissions.SERVERQUESTADMIN_COMMAND);

		addSubCommand("setStepSchematic", new ServerQuestAdminSetStepSchematicSubCommand());
	}
}