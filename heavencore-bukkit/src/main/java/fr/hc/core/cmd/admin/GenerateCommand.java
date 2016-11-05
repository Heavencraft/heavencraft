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

		ChatUtil.sendMessage(sender, "Génération de la carte [%1$s}...", world.getName());

		long i = 0;

		for (int x = minChunk.getX(); x <= maxChunk.getX(); x++)
			for (int z = minChunk.getZ(); z <= maxChunk.getZ(); z++)
				new GenerateChunkTask(world.getName(), x, z, i++ / CHUNKS_PER_TICK);

		ChatUtil.sendMessage(sender, "La carte {%1$s} sera complètement générée dans environ {%2$s} secondes.",
				world.getName(), i / CHUNKS_PER_TICK / 20);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{generate} <carte> <taille>");
	}

	private class GenerateChunkTask extends BukkitRunnable
	{
		private final String worldName;
		private final int x;
		private final int z;

		public GenerateChunkTask(String worldName, int x, int z, long delay)
		{
			runTaskLater(plugin, delay);

			this.worldName = worldName;
			this.x = x;
			this.z = z;
		}

		@Override
		public void run()
		{
			final World world = Bukkit.getWorld(worldName);

			// Chunk is already loaded, no need to generate it
			if (world.isChunkLoaded(x, z))
				return;

			world.loadChunk(x, z);
			new UnloadChunkTask(worldName, x, z);

			log.info("Chunk generated: {}, {}, {}", worldName, x, z);
		}
	}

	private class UnloadChunkTask extends BukkitRunnable
	{
		private final String worldName;
		private final int x;
		private final int z;

		public UnloadChunkTask(String worldName, int x, int z)
		{
			runTaskLater(plugin, 20);

			this.worldName = worldName;
			this.x = x;
			this.z = z;
		}

		@Override
		public void run()
		{
			Bukkit.getWorld(worldName).unloadChunk(x, z);
		}
	}
}