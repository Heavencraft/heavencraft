package fr.hc.rp.cmd.companies;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.InputUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.companies.Company;

public class EntrepriseInfoSubCommand extends SubCommand
{
	private final HeavenRP plugin = HeavenRPInstance.get();

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(sender);
			return;
		}

		final String companyName = args[0];
		final Company company = plugin.getCompanyProvider().getCompanyByTag(companyName);

		ChatUtil.sendMessage(sender, "Nom : %1$s", company.getName());
		ChatUtil.sendMessage(sender, "Employeurs : %1$s", InputUtil.userIdsToString(company.getEmployers(), ", "));
		ChatUtil.sendMessage(sender, "Employés : %1$s", InputUtil.userIdsToString(company.getEmployees(), ", "));
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{entreprise} info <nom de l'entreprise>");
	}
}