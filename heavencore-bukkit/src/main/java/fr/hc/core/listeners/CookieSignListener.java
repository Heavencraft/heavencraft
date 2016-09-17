package fr.hc.core.listeners;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.hc.core.AbstractSignListener;
import fr.hc.core.BukkitHeavenCore;
import fr.hc.core.exceptions.HeavenException;

public class CookieSignListener extends AbstractSignListener
{
	private static final Random rand = new Random();

	public CookieSignListener(BukkitHeavenCore plugin)
	{
		super(plugin, "Cookie", "hc.core.CookieSign");
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		if (rand.nextInt(10) != 0)
			return;

		ItemStack cookie = new ItemStack(Material.COOKIE);
		ItemMeta meta = cookie.getItemMeta();
		meta.setDisplayName("Cookie de l'amitié");
		cookie.setItemMeta(meta);
		player.getInventory().addItem(cookie);
		player.updateInventory();
	}
}
