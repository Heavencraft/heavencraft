package fr.hc.rp.economy.stores;

import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.core.utils.BukkitUtil;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.db.companies.Company;
import fr.hc.rp.db.stocks.CompanyIdAndStockName;
import fr.hc.rp.db.stocks.Stock;
import fr.hc.rp.db.stores.RemoveStoreQuery;
import fr.hc.rp.db.stores.Store;
import fr.hc.rp.db.users.RPUser;

public class MagasinSignListener extends AbstractStoreSignListener
{
	public MagasinSignListener(BukkitHeavenRP plugin)
	{
		super(plugin, "Magasin", "");
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		if (event.getBlock().getType() != Material.WALL_SIGN)
			return false;

		final String companyName = event.getLine(COMPANY_LINE);

		final Optional<RPUser> optUser = plugin.getUserProvider().getOptionalUserByUniqueId(player.getUniqueId());
		if (!optUser.isPresent())
			throw new UserNotFoundException(player.getUniqueId());
		final RPUser user = optUser.get();

		final Company company = plugin.getCompanyProvider().getCompanyByTag(companyName);

		if (!company.isEmployeeOrEmployer(user))
			throw new HeavenException("Vous n'êtes pas employé de cette entreprise.");

		final String stockName = event.getLine(NAME_LINE);

		final String quantityPrice = event.getLine(QUANTITY_PRICE_LINE);
		final int indexOfSeparator = quantityPrice.indexOf(QUANTITY_PRICE_SEPARATOR);
		final int quantity = ConversionUtil.toUint(quantityPrice.substring(0, indexOfSeparator));
		final int price = ConversionUtil.toUint(quantityPrice.substring(indexOfSeparator + 1));

		final boolean isBuyer = false;
		final HeavenBlockLocation location = ConversionUtil.toHeavenBlockLocation(event.getBlock().getLocation());

		plugin.getStoreProvider().createStore(new CompanyIdAndStockName(company.getId(), stockName), quantity, price,
				isBuyer, location);
		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		final HeavenBlockLocation location = ConversionUtil.toHeavenBlockLocation(sign.getBlock().getLocation());

		final Store store = plugin.getStoreProvider().getStoreByLocation(location);
		if (!store.hasStockId())
		{
			ChatUtil.sendMessage(player, "Ce magasin n'a actuellement pas de coffre lié.");
			return;
		}

		final RPUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		if (user.getBalance() < store.getPrice())
		{
			ChatUtil.sendMessage(player, "Vous fouillez dans votre bourse... Vous n'avez pas assez.");
			return;
		}

		final Stock stock = plugin.getStockProvider().getStockById(store.getStockId());
		final Chest chest = getStockChest(stock);
		if (chest == null)
		{
			ChatUtil.sendMessage(player, "Le coffre lié à ce magasin n'existe plus.");
			return;
		}

		final String content = getStoreContentString(chest);
		if (content == null)
		{
			ChatUtil.sendMessage(player, "Le coffre lié à ce magasin est vide ou incohérent.");
			return;
		}

		ChatUtil.sendMessage(player, "Le coffre contient %1$s.", content);
	}

	@Override
	protected boolean onSignBreak(Player player, Sign sign) throws HeavenException
	{
		final HeavenBlockLocation location = ConversionUtil.toHeavenBlockLocation(sign.getLocation());

		final Optional<Store> optStock = plugin.getStoreProvider().getOptionalStoreByLocation(location);
		if (!optStock.isPresent())
			return true;

		new RemoveStoreQuery(optStock.get(), plugin.getStoreProvider())
		{
			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();

		return true;
	}

	private static String getStoreContentString(Chest chest)
	{
		final Inventory inventory = chest.getInventory();
		ItemStack storeItem = null;

		for (final ItemStack item : inventory)
		{
			if (item == null)
				continue;

			if (storeItem == null)
			{
				storeItem = item;
				continue;
			}

			if (!item.isSimilar(storeItem))
				return null;
		}

		if (storeItem == null)
			return null;

		final StringBuilder builder = ConversionUtil.localBuilder.get();
		builder.append(storeItem.getType().name());
		for (final Entry<Enchantment, Integer> enchant : storeItem.getEnchantments().entrySet())
		{
			builder.append(' ').append(enchant.getKey().getName()).append(" lvl ").append(enchant.getValue());
		}

		return builder.toString();
	}

	private static Chest getStockChest(Stock stock)
	{
		final Block chest = ConversionUtil.toLocation(stock.getChestLocation()).getBlock();

		if (!BukkitUtil.isChest(chest.getType()))
			return null;

		return (Chest) chest.getState();
	}
}
