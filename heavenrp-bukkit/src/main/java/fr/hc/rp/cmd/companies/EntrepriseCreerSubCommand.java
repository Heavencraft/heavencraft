package fr.hc.rp.cmd.companies;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.RPPermissions;
import fr.hc.rp.db.companies.Company;

public class EntrepriseCreerSubCommand extends SubCommand
{
	private final HeavenRP plugin = HeavenRPInstance.get();

	public EntrepriseCreerSubCommand()
	{
		super(RPPermissions.ENTREPRISE_CREER_COMMAND);
	}

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 2)
		{
			sendUsage(sender);
			return;
		}

		final String name = args[0];
		final String tag = args[1];

		final Company company = plugin.getCompanyProvider().createCompany(name, tag);

		ChatUtil.sendMessage(sender, "L'entreprise {%1$s} a été crée avec succès.", company);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{entreprise} creer <nom de l'entreprise> <tag de l'entreprise>");
	}
}