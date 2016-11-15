package fr.hc.rp.towns;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.listeners.sign.AbstractSignListener;
import fr.hc.core.tasks.queries.BatchQuery;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.HeavenGuardInstance;
import fr.hc.guard.db.regions.AddMemberQuery;
import fr.hc.guard.db.regions.Region;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.bankaccounts.BankAccount;
import fr.hc.rp.db.bankaccounts.BankAccountMoneyTransfertQuery;
import fr.hc.rp.db.towns.Town;

public class ParcelleSignListener extends AbstractSignListener
{
	private static final int TOWN_LINE = 1;
	private static final int PRICE_LINE = 2;

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
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		final Collection<Region> regions = HeavenGuardInstance.get().getRegionManager()
				.getRegionsAtLocationWithoutParents(sign.getWorld().getName(), sign.getX(), sign.getY(), sign.getZ());

		if (regions.isEmpty())
			throw new HeavenException("Il n'y a pas de parcelle à acheter ici.");

		if (regions.size() != 1)
			throw new HeavenException("Il y a plus d'une parcelle à cet endroit.");

		final Region region = regions.iterator().next();
		final User user = HeavenRPInstance.get().getUserProvider().getUserByUniqueId(player.getUniqueId());
		final Town town = HeavenRPInstance.get().getTownProvider().getTownByName(sign.getLine(TOWN_LINE));
		final BankAccount townAccount = HeavenRPInstance.get().getBankAccountProvider().getBankAccountByTown(town);

		final int price = ConversionUtil.toUint(sign.getLine(PRICE_LINE));

		new BatchQuery(new BankAccountMoneyTransfertQuery(user, townAccount, price),
				new AddMemberQuery(region, user, true, HeavenGuardInstance.get().getConnectionProvider()))
		{
			@Override
			public void onSuccess()
			{
				sign.getBlock().setType(Material.AIR);
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();
	}

	@Override
	protected boolean onSignBreak(Player player, Sign sign) throws HeavenException
	{
		return true;
	}
}