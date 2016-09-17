package fr.hc.core.commands;

import java.sql.SQLException;
import java.util.Optional;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.BukkitHeavenCore;
import fr.hc.core.HeavenCore;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.db.users.home.IncrementHomeNumberQuery;
import fr.hc.core.db.users.home.UserWithHome;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;

public class BuyHomeCommand extends AbstractCommandExecutor
{

	private final HeavenCore plugin;

	public BuyHomeCommand(BukkitHeavenCore plugin)
	{
		super(plugin, "buyhome");
		this.plugin = plugin;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{

		final Optional<? extends UserWithHome> optUser = plugin.getUserProvider()
				.getUserByUniqueId(player.getUniqueId());
		if (!optUser.isPresent())
			throw new HeavenException("L'UUID n'est pas liée a un compte heavencraft. Contactez un administrateur.");

		int price = 0;
		if (optUser.get().getHomeNumber() >= 2)
			price = 1000 * (optUser.get().getHomeNumber() - 2) + 1000;

		// TODO implement the money withdrew, and transfer it to the Heavencraft Account.
		throw new NotImplementedException();
		// final Optional<? extends UserWithBalance> balencedUser = provider.getUserByUniqueId(player.getUniqueId());
		//
		// try
		// {
		// new UpdateUserBalanceQuery(balencedUser.get(), -price, provider).executeQuery();
		// }
		// catch (final SQLException e)
		// {
		// throw new HeavenException(e.getMessage());
		// }

//		try
//		{
//			new IncrementHomeNumberQuery(optUser.get(), plugin.getUserProvider()).executeQuery();
//		}
//		catch (SQLException e)
//		{
//			log.error(e.getMessage());
//			throw new DatabaseErrorException();
//		}
//		ChatUtil.sendMessage(player, "Vous venez d'acheter un nouveau home au prix de {%1$d} Po(s).", price);
//		return;
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est que utilisable en jeu");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{sethome} <numéro du home>");
	}

}
