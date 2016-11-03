package fr.heavencraft.heavenproxy.jit;

import java.io.File;
import java.io.IOException;

import fr.heavencraft.heavenproxy.ProxyLogger;

public enum ServerProcess
{
	// Prod
	// Fun("fun", ServerProcessManager._2G, "/home/minecraft/prod/servers/fun"),
	UltraHard("ultrahard", ServerProcessManager._1G, "/home/minecraft/prod/servers/ultrahard"),
	Musee("musee", ServerProcessManager._512M, "/home/minecraft/prod/servers/musee"),
	Event("event", ServerProcessManager._1G, "/home/minecraft/prod/servers/event"),
	// Mini-jeux
	Infected("infected", ServerProcessManager._1G, "/home/minecraft/prod/minigames/infected"),
	MarioKart("mariokart", ServerProcessManager._1G, "/home/minecraft/prod/minigames/mariokart"),
	Paintball("paintball", ServerProcessManager._3G, "/home/minecraft/prod/minigames/paintball"),
	TNTRun("tntrun", ServerProcessManager._1G, "/home/minecraft/prod/minigames/tntrun"),
	// UAT
	UAT_SemiRP("uat-semirp", ServerProcessManager._2G, "/home/minecraft/uat/servers/semirp"),
	UAT_Creative("uat-creative", ServerProcessManager._512M, "/home/minecraft/uat/servers/creative"),
	UAT_Survival("uat-survival", ServerProcessManager._512M, "/home/minecraft/uat/servers/survival"),
	Build("build", ServerProcessManager._2G, "/home/minecraft/uat/servers/build");

	public static ServerProcess getUniqueInstanceByName(String name)
	{
		for (final ServerProcess serverProcess : ServerProcess.values())
		{
			if (serverProcess.name.equals(name))
			{
				return serverProcess;
			}
		}
		return null;
	}

	private static final ProxyLogger log = ProxyLogger.getLogger(ServerProcess.class);

	// Bash scripts
	private static final String START_SERVER = "/home/minecraft/scripts/start_server.sh";
	private static final String STOP_SERVER = "/home/minecraft/scripts/stop_server.sh";
	// Log messages
	private static final String STARTING_SERVER = "Starting server %1$s with %2$s MB on %3$s";
	private static final String STOPPING_SERVER = "Stopping server %1$s";
	private static final String SERVER_STARTED = "Server %1$s started";
	private static final String SERVER_STOPPED = "Server %1$s stopped";

	private final String name;
	private final int memory;
	private final File path;
	private long lastStart = 0;

	private ServerProcess(String name, int memory, String path)
	{
		this.name = name;
		this.memory = memory;
		this.path = new File(path).getAbsoluteFile();
	}

	public boolean start()
	{
		// Last start is not 30s ago ! do nothing
		if (System.currentTimeMillis() - lastStart < 30000)
		{
			log.info("Server %1$s already started recently.", name);
			return true;
		}

		log.info(STARTING_SERVER, name, memory, path);

		try
		{
			new ProcessBuilder(START_SERVER, name, memory + "M").directory(path).start();
			log.info(SERVER_STARTED, name);
			lastStart = System.currentTimeMillis();
			return true;
		}
		catch (final IOException ex)
		{
			log.error("Cannot start server %1$s", name);
			ex.printStackTrace();
			return false;
		}
	}

	public boolean stop()
	{
		log.info(STOPPING_SERVER, name);

		try
		{
			new ProcessBuilder(STOP_SERVER, name).start();
			log.info(SERVER_STOPPED, name);
			return true;
		}
		catch (final IOException ex)
		{
			log.error("Cannot stop server %1$s", name);
			ex.printStackTrace();
			return false;
		}
	}

	public boolean hasEnoughtMemory()
	{
		final long freeMemory = SystemHelper.getFreeMemoryMb();
		log.info("Free memory : %1$s, needed memory : %2$s", freeMemory, memory);
		return freeMemory > memory;
	}
}