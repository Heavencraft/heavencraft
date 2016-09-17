package fr.hc.rp.cmd.towns;

import fr.hc.core.cmd.SubCommandsCommand;
import fr.hc.rp.BukkitHeavenRP;

public class VilleCommand extends SubCommandsCommand
{
	public VilleCommand(BukkitHeavenRP plugin)
	{
		super(plugin, "ville");

		addSubCommand("info", new VilleInfoSubCommand());
		addSubCommand("ajouter", new VilleAjouterSubCommand());
		addSubCommand("retirer", new VilleRetirerSubCommand());
	}
}