package fr.hc.core.cmd.homes;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.BukkitHeavenCore;
import fr.hc.core.HeavenCore;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.db.users.balance.UpdateUserBalanceQuery;
import fr.hc.core.db.users.home.IncrementHomeNumberQuery;
import fr.hc.core.db.users.home.UserWithHome;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.BatchQuery;
import fr.hc.core.utils.chat.ChatUtil;

public class BuyHomeCommand extends AbstractCommandExecutor
{
	private static final String PRICE = "Votre home {%1$s} vous coûtera {%1$s} pièces d'or. Faites /buyhome valider pour confirmer l'achat.";
	private static final String SUCCESS = "Vous venez d'acheter votre home {%1$s}.";

	private final HeavenCore plugin;

	public BuyHomeCommand(BukkitHeavenCore plugin)
	{
		super(plugin, "buyhome");
		this.plugin = plugin;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		final UserProvider<? extends UserWithHome> userProvider = plugin.getUserProvider();
		final Optional<? extends UserWithHome> optUser = userProvider.getUserByUniqueId(player.getUniqueId());
		if (!optUser.isPresent())
			throw new HeavenException("L'UUID n'est pas liée a un compte heavencraft. Contactez un administrateur.");

		final UserWithHome user = optUser.get();

		final int homeNumber = user.getHomeNumber() + 1;
		final int price = getHomePrice(homeNumber);

		if (args.length == 0 || !args[0].equalsIgnoreCase("valider"))
		{
			ChatUtil.sendMessage(player, PRICE, homeNumber, price);
			return;
		}

		new BatchQuery(new UpdateUserBalanceQuery(user, -price, userProvider),
				new IncrementHomeNumberQuery(user, userProvider))
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, SUCCESS, homeNumber);
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}

		}.schedule();
	}

	private static int getHomePrice(int homeNumber)
	{
		return (homeNumber - 3) * 1000 + 1000;
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