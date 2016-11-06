package fr.hc.core.cmd.admin;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.CorePermissions;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class GenerateCommand extends AbstractCommandExecutor
{
	private static final int CHUNKS_PER_TICK = 100;

	private final Logger log = LoggerFactory.getLogger(getClass());

	public GenerateCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "generate", CorePermissions.GENERATE_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 2)
		{
			sendUsage(sender);
			return;
		}

		final World world = Bukkit.getWorld(args[0]);
		final int size = ConversionUtil.toUint(args[1]);

		final Chunk minChunk = world.getChunkAt(new Location(world, -size, 100, -size));
		final Chunk maxChunk = world.getChunkAt(new Location(world, size, 100, size));

		new GenerateChunkTask(world, minChunk, maxChunk);
		ChatUtil.sendMessage(sender, "Génération de la carte {%1$s}...", world.getName());
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{generate} <carte> <taille>");
	}

	private class GenerateChunkTask extends BukkitRunnable
	{
		private final String worldName;
		private int x;
		private int z;
		private final int minZ;
		private final int maxX;
		private final int maxZ;

		public GenerateChunkTask(World world, Chunk minChunk, Chunk maxChunk)
		{
			runTaskTimer(plugin, 0, 1);

			this.worldName = world.getName();
			this.x = minChunk.getX();
			this.z = this.minZ = minChunk.getZ();
			this.maxX = maxChunk.getX();
			this.maxZ = maxChunk.getZ();

			log.info("GenerateChunkTask: x={}, z={}, maxX={}, maxZ={}", x, z, maxX, maxZ);
		}

		@Override
		public void run()
		{
			int i = 0;

			final World world = Bukkit.getWorld(worldName);

			for (; x <= maxX; x++)
			{
				for (; z <= maxZ; z++)
				{
					// Limit reached, we continue next tick
					if (++i > CHUNKS_PER_TICK)
						return;

					// Chunk is already loaded, no need to generate it
					if (world.isChunkLoaded(x, z))
					{
						log.info("Chunk already loaded: {}, {}, {}", worldName, x, z);
						continue;
					}

					world.loadChunk(x, z);
					log.info("Chunk loaded: {}, {}, {}", worldName, x, z);
				}

				z = minZ;
			}

			log.info("Map {} generated", worldName);
			cancel();

			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
		}
	}
}