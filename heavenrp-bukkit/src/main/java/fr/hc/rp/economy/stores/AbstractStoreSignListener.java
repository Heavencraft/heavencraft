package fr.hc.rp.economy.stores;

import java.util.Optional;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.listeners.sign.AbstractSignWithConfirmationListener;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.companies.Company;
import fr.hc.rp.db.stocks.CompanyIdAndStockName;
import fr.hc.rp.db.stores.RemoveStoreQuery;
import fr.hc.rp.db.stores.Store;
import fr.hc.rp.db.users.RPUser;

public abstract class AbstractStoreSignListener extends AbstractSignWithConfirmationListener
{
	protected static final int COMPANY_LINE = 1;
	protected static final int NAME_LINE = 2;
	protected static final int QUANTITY_PRICE_LINE = 3;
	protected static final char QUANTITY_PRICE_SEPARATOR = '@';

	protected final HeavenRP plugin = HeavenRPInstance.get();
	protected final boolean isBuyer;

	public AbstractStoreSignListener(JavaPlugin plugin, String tag, boolean isBuyer)
	{
		super(plugin, tag, "");
		this.isBuyer = isBuyer;
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		final String companyName = event.getLine(COMPANY_LINE);
		final Company company = plugin.getCompanyProvider().getCompanyByTag(companyName);

		final RPUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		if (!company.isEmployeeOrEmployer(user))
			throw new HeavenException("Vous n'êtes pas employé de cette entreprise.");

		final String quantityPrice = event.getLine(QUANTITY_PRICE_LINE);
		final int indexOfSeparator = quantityPrice.indexOf(QUANTITY_PRICE_SEPARATOR);
		final int quantity = ConversionUtil.toUint(quantityPrice.substring(0, indexOfSeparator));
		final int price = ConversionUtil.toUint(quantityPrice.substring(indexOfSeparator + 1));

		final String stockName = event.getLine(NAME_LINE);
		final CompanyIdAndStockName companyIdAndStockName = new CompanyIdAndStockName(company.getId(), stockName);
		final HeavenBlockLocation location = ConversionUtil.toHeavenBlockLocation(event.getBlock().getLocation());

		plugin.getStoreProvider().createStore(companyIdAndStockName, quantity, price, isBuyer, location);
		return true;
	}

	@Override
	protected void onFirstClick(Player player, Sign sign) throws HeavenException
	{
		onStoreSignClick(player, sign, true);
	}

	@Override
	protected void onSecondClick(Player player, Sign sign) throws HeavenException
	{
		onStoreSignClick(player, sign, false);
	}

	protected abstract void onStoreSignClick(Player player, Sign sign, boolean firstClick) throws HeavenException;

	@Override
	protected boolean onSignBreak(Player player, Sign sign) throws HeavenException
	{
		final HeavenBlockLocation location = ConversionUtil.toHeavenBlockLocation(sign.getLocation());

		final Optional<Store> optStore = plugin.getStoreProvider().getOptionalStoreByLocation(location);
		if (!optStore.isPresent())
			return true;
		final Store store = optStore.get();

		new RemoveStoreQuery(store, plugin.getStoreProvider())
		{
			@Override
			public void onSuccess()
			{
				// TODO: Notify all connected user of the company
				ChatUtil.sendMessage(player, "Le magasin %1$s a été détruit.", store);
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();

		return true;
	}
}