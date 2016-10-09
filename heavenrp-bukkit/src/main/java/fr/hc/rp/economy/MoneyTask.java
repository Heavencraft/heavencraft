package fr.hc.rp.economy;

import java.util.Calendar;
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
import fr.hc.rp.db.users.RPUser;

public class MoneyTask extends BukkitRunnable
{
	private static final long PERIOD = 12000; // 10 minutes : 20 * 60 * 10 ticks

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final HeavenRP plugin;

	public MoneyTask(BukkitHeavenRP plugin)
	{
		runTaskTimer(plugin, PERIOD, PERIOD);
		this.plugin = plugin;
	}

	@Override
	public void run()
	{
		final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		if (players.isEmpty())
			return;

		final int amount = getAmount();
		log.info("Giving {} po to [{}]", amount, ConversionUtil.toString(players));

		try
		{
			for (final Player player : players)
			{
				final Optional<RPUser> optUser = plugin.getUserProvider().getOptionalUserByUniqueId(player.getUniqueId());
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

	private static int getAmount()
	{
		final Calendar date = Calendar.getInstance();

		switch (date.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.SATURDAY:
			case Calendar.SUNDAY:
				return date.get(Calendar.HOUR_OF_DAY) < 18 ? 2 : 3;

			default:
				return date.get(Calendar.HOUR_OF_DAY) < 18 ? 1 : 2;
		}
	}
}