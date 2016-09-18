package fr.hc.rp.cmd.parcelle;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.cmd.SubCommand;
import fr.hc.core.cmd.SubCommandsCommand;

public class ParcelleCommand extends SubCommandsCommand
{
	public ParcelleCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "parcelle");

		final SubCommand creerSubCommand = new ParcelleCreerSubCommnad();
		addSubCommand("creer", creerSubCommand);
		addSubCommand("créer", creerSubCommand);
		addSubCommand("supprimer", new ParcelleSupprimerSubCommand());
	}
}