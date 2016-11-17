package fr.hc.rp.towns;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.listeners.sign.AbstractSignWithConfirmationListener;
import fr.hc.core.tasks.queries.BatchQuery;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.HeavenGuard;
import fr.hc.guard.HeavenGuardInstance;
import fr.hc.guard.db.regions.AddMemberQuery;
import fr.hc.guard.db.regions.Region;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.bankaccounts.BankAccount;
import fr.hc.rp.db.bankaccounts.BankAccountMoneyTransfertQuery;
import fr.hc.rp.db.users.RPUser;

public class ParcelleSignListener extends AbstractSignWithConfirmationListener
{
	private static final int TOWN_LINE = 1;
	private static final int PRICE_LINE = 2;

	private final HeavenRP plugin = HeavenRPInstance.get();
	private final HeavenGuard guard = HeavenGuardInstance.get();

	public ParcelleSignListener(JavaPlugin plugin)
	{
		super(plugin, "Parcelle");
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		return false; // Placed via /parcelle commmand
	}

	@Override
	protected void onFirstClick(Player player, Sign sign) throws HeavenException
	{
		final int price = ConversionUtil.toUint(sign.getLine(PRICE_LINE));
		final Region region = getBuyableRegion(sign.getLocation());
		getSolvableUser(player, price);

		ChatUtil.sendMessage(player, "Vous allez acheter la parcelle {%1$s} pour {%2$s}.", region, price);
	}

	@Override
	protected void onSecondClick(Player player, Sign sign) throws HeavenException
	{
		final int price = ConversionUtil.toUint(sign.getLine(PRICE_LINE));
		final Region region = getBuyableRegion(sign.getLocation());

		final User user = getSolvableUser(player, price);
		final BankAccount townAccount = getTownAccount(sign.getLine(TOWN_LINE));

		new BatchQuery(new BankAccountMoneyTransfertQuery(user, townAccount, price),
				new AddMemberQuery(region, user, true, HeavenGuardInstance.get().getConnectionProvider()))
		{
			@Override
			public void onSuccess()
			{
				sign.getBlock().setType(Material.AIR);
				ChatUtil.sendMessage(player, "Vous êtes désormais propriétaire de la parcelle {%1$s}.", region);
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();
	}

	private Region getBuyableRegion(Location location) throws HeavenException
	{
		final Collection<Region> regions = guard.getRegionManager().getRegionsAtLocationWithoutParents(
				location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());

		if (regions.isEmpty())
			throw new HeavenException("Il n'y a pas de parcelle à acheter ici.");

		if (regions.size() != 1)
			throw new HeavenException("Il y a plus d'une parcelle à cet endroit.");

		return regions.iterator().next();
	}

	private BankAccount getTownAccount(String name) throws HeavenException
	{
		return plugin.getBankAccountProvider().getBankAccountByTown(plugin.getTownProvider().getTownByName(name));
	}

	private User getSolvableUser(Player player, int price) throws HeavenException
	{
		final RPUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		if (user.getBalance() < price)
			throw new HeavenException("Vous fouillez dans votre bourse... Vous n'avez pas assez.");
		return user;
	}

	@Override
	protected boolean onSignBreak(Player player, Sign sign) throws HeavenException
	{
		return true;
	}
}