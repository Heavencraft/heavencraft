package fr.hc.rp.economy.stores;

import java.util.Collection;

import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.BukkitConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.db.bankaccounts.BankAccount;
import fr.hc.rp.db.bankaccounts.BankAccountMoneyTransfertQuery;
import fr.hc.rp.db.stores.Store;
import fr.hc.rp.db.users.RPUser;

public class AchatSignListener extends AbstractStoreSignListener
{
	public AchatSignListener(JavaPlugin plugin)
	{
		super(plugin, "Achat", true);
	}

	@Override
	protected void onStoreSignClick(Player player, Sign sign, boolean firstClick) throws HeavenException
	{
		final HeavenBlockLocation location = BukkitConversionUtil.toHeavenBlockLocation(sign.getBlock().getLocation());

		final Store store = plugin.getStoreProvider().getStoreByLocation(location);

		final RPUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());

		final BankAccount bankAccount = plugin.getBankAccountProvider().getBankAccountByCompany(
				plugin.getCompanyProvider().getCompanyById(store.getCompanyIdAndStockName().getCompanyId()));

		if (bankAccount.getBalance() < store.getPrice())
			throw new HeavenException("Ce commerçant est trop pauvre pour pouvoir vous payer.");

		final Chest chest = BukkitStoreUtil.getStoreChest(store);
		final ItemStack storeItem = BukkitStoreUtil.getStoreItem(chest);

		if (BukkitStoreUtil.getItemQuantityInInventory(player.getInventory(), storeItem) < store.getQuantity())
			throw new HeavenException("Vous n'avez pas assez dans votre inventaire.");

		if (chest.getInventory().firstEmpty() == -1)
			throw new HeavenException("Le coffre lié à ce magasin est plein !");

		final String content = BukkitStoreUtil.getStoreContentString(chest);

		if (firstClick)
		{
			ChatUtil.sendMessage(player, "Vous vous apprêtez à vendre {%1$s %2$s} pour {%3$s} pièces d'or.",
					store.getQuantity(), content, store.getPrice());
			return;
		}

		new BankAccountMoneyTransfertQuery(bankAccount, user, store.getPrice())
		{
			@Override
			public void onSuccess()
			{
				try
				{
					final Collection<ItemStack> removeItems = BukkitStoreUtil
							.removeItemFromInventory(player.getInventory(), storeItem, store.getQuantity());

					for (final ItemStack removedItem : removeItems)
						chest.getInventory().addItem(removedItem);
					player.updateInventory();

					ChatUtil.sendMessage(player, "Vous avez vendu {%1$s %2$s} pour {%3$s} pièces d'or.",
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