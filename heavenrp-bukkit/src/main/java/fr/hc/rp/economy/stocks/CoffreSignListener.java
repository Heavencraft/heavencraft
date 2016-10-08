package fr.hc.rp.economy.stocks;

import java.util.Optional;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.hc.core.AbstractSignListener;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.db.companies.Company;
import fr.hc.rp.db.stocks.CompanyIdAndStockName;
import fr.hc.rp.db.stocks.Stock;
import fr.hc.rp.db.stores.RemoveStockQuery;
import fr.hc.rp.db.users.RPUser;

public class CoffreSignListener extends AbstractSignListener
{
	private static final String COFFRE_TAG = "Coffre";
	private static final int COMPANY_LINE = 1;
	private static final int NAME_LINE = 2;

	private final BukkitHeavenRP plugin;

	public CoffreSignListener(BukkitHeavenRP plugin)
	{
		super(plugin, COFFRE_TAG, "");

		this.plugin = plugin;
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		final String companyName = event.getLine(COMPANY_LINE);

		final Optional<RPUser> optUser = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		if (!optUser.isPresent())
			throw new UserNotFoundException(player.getUniqueId());
		final RPUser user = optUser.get();

		final Company company = plugin.getCompanyProvider().getCompanyByTag(companyName);

		if (!company.isEmployeeOrEmployer(user))
			throw new HeavenException("Vous n'êtes pas employé de cette entreprise.");

		final String stockName = event.getLine(NAME_LINE);
		plugin.getStockProvider().createStock(new CompanyIdAndStockName(company.getId(), stockName),
				ConversionUtil.toHeavenBlockLocation(event.getBlock().getLocation()));
		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		ChatUtil.sendMessage(player, "Yep, ceci est un coffre..");
	}

	@Override
	protected boolean onSignBreak(Player player, Sign sign) throws HeavenException
	{
		final Stock stock = plugin.getStockProvider()
				.getStockByLocation(ConversionUtil.toHeavenBlockLocation(sign.getLocation()));

		new RemoveStockQuery(stock, plugin.getStockProvider())
		{
			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();

		return true;
	}
}