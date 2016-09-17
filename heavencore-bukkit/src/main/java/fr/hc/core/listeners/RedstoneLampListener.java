package fr.hc.core.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;

public class RedstoneLampListener extends AbstractBukkitListener
{

	public RedstoneLampListener(AbstractBukkitPlugin plugin)
	{
		super(plugin);
	}

	private static final BlockFace[] FACES = { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST,
			BlockFace.SOUTH, BlockFace.WEST };

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		final Block block = event.getClickedBlock();

		if (block.getType() != Material.REDSTONE_LAMP_OFF)
			return;

		if (!event.hasItem() || event.getItem().getType() != Material.FLINT_AND_STEEL)
			return;

		// For each block next to the redstone lamp
		for (final BlockFace face : FACES)
		{
			final Block relative = block.getRelative(face);

			// Do not replace block containing inventory
			if (!(relative.getState() instanceof InventoryHolder))
			{
				final BlockState state = relative.getState();
				relative.setType(Material.REDSTONE_BLOCK); // Replace block
				state.update(true); // Restore previous state
				break;
			}
		}

		if (block.getType() != Material.REDSTONE_LAMP_ON)
		{
			log.error("RedstoneLampListener is not working properly. Failed to replace block at %1$s %2$s %3$s %4$s", //
					block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
		}

		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockRedstone(BlockRedstoneEvent event)
	{
		final Block block = event.getBlock();

		if (block.getType() != Material.REDSTONE_LAMP_ON)
			return;

		if (event.getNewCurrent() != 0)
			return;

		for (final BlockFace f : BlockFace.values())
		{
			final Block block2 = block.getRelative(f);

			if (block2 != null && isRedstoneMaterial(block2.getType()))
				return;
		}

		event.setNewCurrent(15);
	}

	private static boolean isRedstoneMaterial(Material m)
	{
		switch (m)
		{
			case DETECTOR_RAIL:
			case REDSTONE_WIRE:
			case REDSTONE_TORCH_OFF:
			case REDSTONE_TORCH_ON:
			case DIODE_BLOCK_OFF:
			case DIODE_BLOCK_ON:
			case LEVER:
			case STONE_BUTTON:
			case WOOD_BUTTON:
			case ACTIVATOR_RAIL:
			case DAYLIGHT_DETECTOR:
			case REDSTONE_BLOCK:
			case REDSTONE_COMPARATOR_OFF:
			case REDSTONE_COMPARATOR_ON:
				return true;
			default:
				return false;
		}
	}
}
