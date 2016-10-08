package fr.hc.rp.cmd.companies;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.cmd.SubCommandsCommand;
import fr.hc.rp.BukkitHeavenRP;

public class EntrepriseCommand extends SubCommandsCommand
{
	public EntrepriseCommand(BukkitHeavenRP plugin)
	{
		super(plugin, "entreprise");

		addSubCommand("info", new EntrepriseInfoSubCommand());
		addSubCommand("ajouterEmployeur", new EntrepriseAjouterEmployeurSubCommand());
		addSubCommand("retirerEmployeur", new RetirerEmployeurSubCommand());

		final EntrepriseCreerSubCommand creer = new EntrepriseCreerSubCommand();
		addSubCommand("créer", creer);
		addSubCommand("creer", creer);

		final SubCommand ajouterEmploye = new EntrepriseAjouterEmployeSubCommand();
		addSubCommand("ajouterEmployé", ajouterEmploye);
		addSubCommand("ajouterEmploye", ajouterEmploye);

		final SubCommand retirerEmploye = new RetirerEmployeSubCommand();
		addSubCommand("retirerEmployé", retirerEmploye);
		addSubCommand("retirerEmploye", retirerEmploye);
	}
}