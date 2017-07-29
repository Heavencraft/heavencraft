package fr.hc.rp.economy;

import java.util.Arrays;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.db.users.balance.UpdateUserBalanceQuery;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.db.users.RPUser;

public class GoldDropListener extends AbstractBukkitListener
{
	private static final Material GOLD_MATERIAL = Material.GOLD_NUGGET;
	private static final String GOLD_NAME = ChatColor.GOLD + "Pièce d'or";

	private final HeavenRP plugin;

	public GoldDropListener(BukkitHeavenRP plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	private void onPlayerPickupItem(final EntityPickupItemEvent event)
	{
		if (event.getEntityType() != EntityType.PLAYER)
			return;

		final Player player = (Player) event.getEntity();

		try
		{

			final ItemStack item = event.getItem().getItemStack();

			if (!isGold(item))
				return;

			final int amount = ConversionUtil.toUint(item.getItemMeta().getLore().get(0));
			event.getItem().remove();
			event.setCancelled(true);

			final Optional<RPUser> optUser = plugin.getUserProvider().getOptionalUserByUniqueId(player.getUniqueId());
			if (!optUser.isPresent())
			{
				ChatUtil.sendMessage(player, UnexpectedErrorException.MESSAGE);
				return;
			}

			final RPUser user = optUser.get();
			new UpdateUserBalanceQuery(user, amount, plugin.getUserProvider())
			{
				@Override
				public void onSuccess()
				{
					ChatUtil.sendMessage(player, "Vous venez de trouver {%1$s} pièces d'or par terre.", amount);
				}

				@Override
				public void onException(HeavenException ex)
				{
					ChatUtil.sendMessage(player, ex.getMessage());
				}
			}.schedule();
		}
		catch (final HeavenException ex)
		{
			ChatUtil.sendMessage(player, ex.getMessage());
			log.error("Error while picking up gold.", ex);
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void onInventoryPickupItem(InventoryPickupItemEvent event)
	{
		if (isGold(event.getItem().getItemStack()))
		{
			event.getItem().remove();
			event.setCancelled(true);
		}
	}

	private static boolean isGold(ItemStack item)
	{
		if (item.getType() != GOLD_MATERIAL || !item.hasItemMeta())
			return false;

		final ItemMeta meta = item.getItemMeta();

		if (!meta.hasDisplayName() || !meta.hasLore() || !GOLD_NAME.equals(item.getItemMeta().getDisplayName()))
			return false;

		return true;
	}

	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent event) throws HeavenException
	{
		final Player player = event.getEntity();
		final Optional<RPUser> optUser = plugin.getUserProvider().getOptionalUserByUniqueId(player.getUniqueId());
		if (!optUser.isPresent())
		{
			ChatUtil.sendMessage(player, UnexpectedErrorException.MESSAGE);
			return;
		}

		final RPUser user = optUser.get();
		final int amount = user.getBalance();

		if (amount == 0)
			return;

		new UpdateUserBalanceQuery(user, -amount, plugin.getUserProvider())
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, "Vous avez perdu {%1$s} pièces d'or que vous aviez dans votre bourse.", amount);
				ChatUtil.sendMessage(player, "Pensez à déposer votre argent à la banque la prochaine fois.");

				dropGold(player.getLocation(), amount);
			}
		}.schedule();
	}

	private void dropGold(Location location, int qty)
	{
		final ItemStack item = new ItemStack(GOLD_MATERIAL, 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(GOLD_NAME);
		meta.setLore(Arrays.asList(Integer.toString(qty)));
		item.setItemMeta(meta);
		location.getWorld().dropItem(location, item);
	}
}