package fr.hc.rp.economy.stores;

import java.util.Collection;

import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.db.bankaccounts.BankAccount;
import fr.hc.rp.db.bankaccounts.BankAccountMoneyTransfertQuery;
import fr.hc.rp.db.stores.Store;
import fr.hc.rp.db.users.RPUser;

public class MagasinSignListener extends AbstractStoreSignListener
{
	public MagasinSignListener(BukkitHeavenRP plugin)
	{
		super(plugin, "Magasin", false);
	}

	@Override
	protected void onStoreSignClick(Player player, Sign sign, boolean firstClick) throws HeavenException
	{
		if (player.getInventory().firstEmpty() == -1)
			throw new HeavenException("Vous n'avez pas de place dans votre inventaire !");

		final HeavenBlockLocation location = ConversionUtil.toHeavenBlockLocation(sign.getBlock().getLocation());

		final Store store = plugin.getStoreProvider().getStoreByLocation(location);

		final RPUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		if (user.getBalance() < store.getPrice())
			throw new HeavenException("Vous fouillez dans votre bourse... Vous n'avez pas assez.");

		final Chest chest = BukkitStoreUtil.getStoreChest(store);
		final ItemStack storeItem = BukkitStoreUtil.getStoreItem(chest);

		if (BukkitStoreUtil.getItemQuantityInInventory(chest.getInventory(), storeItem) < store.getQuantity())
			throw new HeavenException("Ce magasin n'a pas assez en stock.");

		final String content = BukkitStoreUtil.getStoreContentString(chest);

		if (firstClick)
		{
			ChatUtil.sendMessage(player, "Vous vous apprêtez à acheter {%1$s %2$s} pour {%3$s} pièces d'or.",
					store.getQuantity(), content, store.getPrice());
			return;
		}

		final BankAccount bankAccount = plugin.getBankAccountProvider().getBankAccountByCompany(
				plugin.getCompanyProvider().getCompanyById(store.getCompanyIdAndStockName().getCompanyId()));

		new BankAccountMoneyTransfertQuery(user, bankAccount, store.getPrice())
		{
			@Override
			public void onSuccess()
			{
				try
				{
					final Collection<ItemStack> removeItems = BukkitStoreUtil
							.removeItemFromInventory(chest.getInventory(), storeItem, store.getQuantity());

					for (final ItemStack removedItem : removeItems)
						player.getInventory().addItem(removedItem);
					player.updateInventory();

					ChatUtil.sendMessage(player, "Vous avez acheté {%1$s %2$s} pour {%3$s} pièces d'or.",
							store.getQuantity(), content, store.getPrice());
				}
				catch (final HeavenException ex)
				{
					ex.printStackTrace();
					ChatUtil.sendMessage(player, ex.getMessage());
				}
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();
	}
}