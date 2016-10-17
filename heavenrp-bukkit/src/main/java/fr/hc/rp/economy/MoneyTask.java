package fr.hc.rp.economy;

import java.util.Collection;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.db.users.balance.UpdateUserBalanceQuery;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.users.RPUser;

public class MoneyTask extends BukkitRunnable
{
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final HeavenRP plugin = HeavenRPInstance.get();

	public MoneyTask(BukkitHeavenRP plugin)
	{
		final long period = plugin.getPricingManager().getMoneyTaskPeriodTicks();
		runTaskTimer(plugin, period, period);
	}

	@Override
	public void run()
	{
		final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		if (players.isEmpty())
			return;

		final int amount = plugin.getPricingManager().getMoneyTaskAmount();
		log.info("Giving {} po to [{}]", amount, ConversionUtil.toString(players));

		try
		{
			for (final Player player : players)
			{
				final Optional<RPUser> optUser = plugin.getUserProvider()
						.getOptionalUserByUniqueId(player.getUniqueId());
				if (!optUser.isPresent())
					throw new UserNotFoundException(player.getUniqueId());

				new UpdateUserBalanceQuery(optUser.get(), amount, plugin.getUserProvider()).schedule();
			}
		}
		catch (final HeavenException ex)
		{
			log.error("Error while giving money", ex);
		}
	}
}