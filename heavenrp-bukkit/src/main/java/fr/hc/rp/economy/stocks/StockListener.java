package fr.hc.rp.economy.stocks;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.BukkitConversionUtil;
import fr.hc.core.utils.BukkitUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.stocks.RemoveStockQuery;
import fr.hc.rp.db.stocks.Stock;

public class StockListener extends AbstractBukkitListener
{
	private final HeavenRP plugin = HeavenRPInstance.get();

	public StockListener(BukkitHeavenRP plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event) throws HeavenException
	{
		final Block block = event.getBlock();
		if (!BukkitUtil.isChest(block.getType()))
			return;

		final HeavenBlockLocation chestLocation = BukkitConversionUtil.toHeavenBlockLocation(block.getLocation());

		final Optional<Stock> optStock = plugin.getStockProvider().getOptionalStockByChestLocation(chestLocation);
		if (!optStock.isPresent())
			return;
		final Stock stock = optStock.get();

		new RemoveStockQuery(stock, plugin.getStockProvider())
		{
			@Override
			public void onSuccess()
			{
				// Don't forget to break the sign
				BukkitConversionUtil.toLocation(stock.getSignLocation()).getBlock().breakNaturally();

				ChatUtil.sendMessage(event.getPlayer(), "Le coffre {%1$s} a été détruit.",
						stock.getCompanyIdAndStockName().getStockName());
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(event.getPlayer(), ex.getMessage());
			}
		}.schedule();
	}
}