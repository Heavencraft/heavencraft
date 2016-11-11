package fr.hc.rp.economy.stores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.utils.BukkitConversionUtil;
import fr.hc.core.utils.BukkitUtil;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.stocks.Stock;
import fr.hc.rp.db.stores.Store;

class BukkitStoreUtil
{

	public static Chest getStoreChest(Store store) throws HeavenException
	{
		if (!store.hasStockId())
			throw new HeavenException("Ce magasin n'a actuellement aucun coffre lié.");

		final Stock stock = HeavenRPInstance.get().getStockProvider().getStockById(store.getStockId());
		final Block chestBlock = BukkitConversionUtil.toLocation(stock.getChestLocation()).getBlock();

		if (!BukkitUtil.isChest(chestBlock.getType()))
			throw new HeavenException("Le coffre lié à ce magasin n'existe plus.");

		return (Chest) chestBlock.getState();
	}

	public static ItemStack getStoreItem(Chest chest) throws HeavenException
	{
		ItemStack storeItem = null;

		for (final ItemStack item : chest.getInventory())
		{
			if (item == null)
				continue;

			if (storeItem == null)
			{
				storeItem = item;
				continue;
			}

			if (!item.isSimilar(storeItem))
				throw new HeavenException("Le coffre lié à ce magain est incohérent.");
		}

		if (storeItem == null)
			throw new HeavenException("Le coffre lié à ce magain est vide.");

		return storeItem;
	}

	public static String getStoreContentString(Chest chest)
	{
		for (final ItemStack item : chest.getInventory())
			if (item != null)
			{
				final StringBuilder builder = ConversionUtil.localBuilder.get();
				builder.append(item.getType().name());
				for (final Entry<Enchantment, Integer> enchant : item.getEnchantments().entrySet())
				{
					builder.append(' ').append(enchant.getKey().getName()).append(" lvl ").append(enchant.getValue());
				}

				return builder.toString();
			}

		return null;
	}

	public static int getItemQuantityInInventory(Inventory inventory, ItemStack storeItem)
	{
		int quantity = 0;
		for (final ItemStack item : inventory)
			if (item != null && item.isSimilar(storeItem))
				quantity += item.getAmount();
		return quantity;
	}

	public static Collection<ItemStack> removeItemFromInventory(Inventory inventory, ItemStack storeItem,
			int quantityToRemove) throws HeavenException
	{
		final Collection<ItemStack> removedItems = new ArrayList<ItemStack>();

		for (int i = 0; i != inventory.getSize() && quantityToRemove != 0; i++)
		{
			final ItemStack item = inventory.getItem(i);

			if (item == null || !item.isSimilar(storeItem))
				continue;

			final int quantityBefore = item.getAmount();
			final int quantityRemoved = Math.min(quantityBefore, quantityToRemove);
			final int quantityAfter = quantityBefore - quantityRemoved;

			final ItemStack removedItem = new ItemStack(item);
			removedItem.setAmount(quantityRemoved);
			removedItems.add(removedItem);

			if (quantityAfter == 0)
				inventory.setItem(i, null);
			else
			{
				item.setAmount(quantityAfter);
				inventory.setItem(i, item);
			}

			quantityToRemove -= quantityRemoved;
		}

		if (quantityToRemove != 0)
			throw new UnexpectedErrorException();

		return removedItems;
	}
}