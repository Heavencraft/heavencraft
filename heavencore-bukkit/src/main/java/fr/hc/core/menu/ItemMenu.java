package fr.hc.core.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;

public class ItemMenu extends AbstractBukkitListener
{
	private static HashMap<UUID, ItemMenu> activeMenus = new HashMap<>();

	private final String title;
	private final int size;
	private final AbstractBukkitPlugin plugin;

	private List<String> titles = new ArrayList<>();
	private List<ItemStack> icons = new ArrayList<>();
	private List<ActionHandler> actions = new ArrayList<>();

	/**
	 * Creates a new Item Menu
	 * 
	 * @param menuTitle
	 *            Title of the menu, allows color codes with '&'
	 * @param slots
	 *            The amount of slots
	 * @param plugin
	 */
	public ItemMenu(String menuTitle, int slots, AbstractBukkitPlugin plugin)
	{
		super(plugin);
		this.title = ChatColor.translateAlternateColorCodes('&', menuTitle);
		this.size = slots;
		this.plugin = plugin;
	}

	/**
	 * Adds a Field to the ItemMenu.
	 * 
	 * @param position
	 *            Slot index in the menu where to place the field.
	 * @param icon
	 *            The icon of the field
	 * @param title
	 *            The title of the field
	 * @param lore
	 *            The description (each line is an argument)
	 * @return
	 */
	public ItemMenu setField(int position, ItemStack icon, String title, String... lore)
	{
		// Do we have the right array size?
		if (position >= titles.size())
		{
			// Create place holders
			for (int i = titles.size(); i <= position; i++)
			{
				titles.add(null);
				icons.add(null);
				actions.add(null);
			}
		}

		title = ChatColor.translateAlternateColorCodes('&', title);
		titles.set(position, title);
		icons.set(position, setItemNameAndLore(icon, title, lore));
		return this; // This allows chaining of .setOption()
	}

	/**
	 * Adds an action, when an item is clicked at this slot.
	 * 
	 * @param position
	 *            The index of the clicked slot.
	 * @param action
	 *            Anonymous function containing logic to execute when clicked.
	 * @return
	 */
	public ItemMenu setAction(int position, ActionHandler action)
	{
		actions.set(position, action);
		return this; // This allows chaining
	}

	/**
	 * Opens an ItemMenu
	 * 
	 * @param p
	 *            The player
	 */
	public void open(Player p)
	{
		final Inventory inv = Bukkit.createInventory(p, this.size, this.title);
		// Build menu
		for (int i = 0; i < icons.size(); i++)
		{
			if (icons.get(i) != null)
				inv.setItem(i, icons.get(i));
		}

		p.openInventory(inv);
		ItemMenu.activeMenus.put(p.getUniqueId(), this);
	}

	/**
	 * Destroys this menu
	 */
	protected void close()
	{
		HandlerList.unregisterAll(this);
		actions.clear();
		actions = null;
		titles.clear();
		titles = null;
		icons.clear();
		icons = null;
	}

	/**
	 * Handles clicks on items, and starts associated actions
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	private void onInventoryClick(InventoryClickEvent event)
	{
		final Inventory inv = event.getInventory();
		if (inv == null)
			return;
		final Player p = (Player) event.getWhoClicked();
		if (p == null)
			return;
		// Is it our inventory? (First check title, then check in hash map)
		final ItemMenu activeMenu = ItemMenu.activeMenus.get(p.getUniqueId());
		if (activeMenu != null && activeMenu == this)
		{
			// Do not let the player pickup/deposit the item.
			event.setCancelled(true);
			// Prepare the action.
			final int slot = event.getRawSlot();
			if (slot >= 0 && slot < size && titles.get(slot) != null)
			{
				if (actions.get(slot) != null)
				{
					// Run the associated action
					final OnClickEvent e = new OnClickEvent(p, slot, titles.get(slot));
					actions.get(slot).onClick(e);

					// Do we want to close the inventory?
					if (e.willClose() || e.NextInventory() != null)
					{

						Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
						{
							@Override
							public void run()
							{
								if (e.NextInventory() != null)
								{
									e.NextInventory().open(p);
								}
								else if (e.willClose() && e.nextInventory == null)
									p.closeInventory();
							}
						}, 1);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClose(InventoryCloseEvent event)
	{
		final Inventory inv = event.getInventory();
		if (inv == null)
			return;
		final Player p = (Player) event.getPlayer();
		if (p == null)
			return;

		// Is it our inventory?
		final ItemMenu activeMenu = ItemMenu.activeMenus.get(p.getUniqueId());
		if (activeMenu != null && activeMenu == this)
		{
			ItemMenu.activeMenus.remove(p.getUniqueId());
			activeMenu.close();
		}
	}

	/**
	 * Set an item's lore
	 * 
	 * @param item
	 * @param name
	 * @param lore
	 * @return
	 */
	protected ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore)
	{
		final ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(lore));
		item.setItemMeta(im);
		return item;
	}

	public interface ActionHandler
	{
		public void onClick(OnClickEvent e);
	}

	public class OnClickEvent
	{
		private final Player p;
		private final int postion;
		private final String name;
		private boolean close;
		private ItemMenu nextInventory;

		public OnClickEvent(Player p, int postion, String name)
		{
			this.p = p;
			this.postion = postion;
			this.name = name;
			this.close = false;
		}

		public Player getPlayer()
		{
			return p;
		}

		public int getPostion()
		{
			return postion;
		}

		public String getName()
		{
			return name;
		}

		public boolean willClose()
		{
			return close;
		}

		/**
		 * Closes the menu
		 */
		public void closeMenu()
		{
			this.close = true;
		}

		public ItemMenu NextInventory()
		{
			return nextInventory;
		}

		/**
		 * Sets a menu that will be opened.
		 * 
		 * @param nextInventory
		 */
		public void setNextMenu(ItemMenu nextInventory)
		{
			this.nextInventory = nextInventory;
		}

	}

}
