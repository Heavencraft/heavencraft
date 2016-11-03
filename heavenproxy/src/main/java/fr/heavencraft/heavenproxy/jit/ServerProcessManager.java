package fr.heavencraft.heavenproxy.jit;

import fr.heavencraft.heavenproxy.AbstractListener;
import fr.heavencraft.heavenproxy.Utils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;

public class ServerProcessManager extends AbstractListener
{
    // Memory (in MB)
    public static int _512M = 512;
    public static int _1G = 1024;
    public static int _2G = 2048;
    public static int _3G = 3072;
    public static int _4G = 4096;

    // Messages d'erreur
    private static final String NOT_ENOUGHT_MEMORY = "Ce monde n'est pas disponible (pas assez de mémoire).";
    private static final String ERROR = "Une erreur s'est produite, merci de contacter un administrateur.";
    private static final String SERVER_STARTING = "Le monde que vous souhaitez accéder est en cours de démarrage, merci de patienter.";

    public ServerProcessManager()
    {
        new StopServerTask();
    }

    @EventHandler
    public void onServerConnect(final ServerConnectEvent event)
    {
        final ServerInfo serverInfo = event.getTarget();
        final String serverName = serverInfo.getName();
        final ServerProcess serverProcess = ServerProcess.getUniqueInstanceByName(serverName);

        // The server is not handled by this system : nothing to do
        if (serverProcess == null)
            return;

        // The server already contains players : nothing to do
        if (!serverInfo.getPlayers().isEmpty())
            return;

        // The server's port is not available, so it is already started : nothing to do
        if (!SystemHelper.isPortAvailable(serverInfo.getAddress().getPort()))
            return;

        event.setCancelled(true);
        final ProxiedPlayer player = event.getPlayer();

        // We don't have enought memory to start the server, send a message to the player
        if (!serverProcess.hasEnoughtMemory())
        {
            Utils.sendMessage(player, NOT_ENOUGHT_MEMORY);
            return;
        }

        if (!serverProcess.start())
        {
            Utils.sendMessage(player, ERROR);
            return;
        }

        Utils.sendMessage(player, SERVER_STARTING);
        new ConnectPlayerTask(player.getName(), serverInfo);
    }
}