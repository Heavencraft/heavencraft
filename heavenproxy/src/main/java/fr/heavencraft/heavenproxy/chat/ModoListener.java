package fr.heavencraft.heavenproxy.chat;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.heavencraft.heavenproxy.AbstractListener;
import fr.heavencraft.heavenproxy.HeavenProxy;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;

public class ModoListener extends AbstractListener
{
    private static final long TIMEOUT = 300; // 5 minutes

    private final Map<String, Long> lastActivity = new HashMap<String, Long>();

    private void update(String player)
    {
        final long currentTimestamp = new Date().getTime() / 1000;
        final Long previousTimestamp = lastActivity.get(player);

        // No previous activity OR previous activity too old
        if (previousTimestamp == null || (currentTimestamp - previousTimestamp) > TIMEOUT)
        {
            // We just update the last activity
            lastActivity.put(player, currentTimestamp);
            return;
        }

        final long deltaTime = currentTimestamp - previousTimestamp;

        // We update the database to add deltaTime
        try
        {
            final PreparedStatement ps = HeavenProxy.getConnection()
                    .prepareStatement("UPDATE modo_stats SET time = time + ? WHERE name = ? LIMIT 1");
            ps.setLong(1, deltaTime);
            ps.setString(2, player);

            if (ps.executeUpdate() != 1)
                log.info("Moderator " + player + " isn't in the database.");
            else
                lastActivity.put(player, currentTimestamp);

        }
        catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event)
    {
        // We watch only moderators
        if (event.getPlayer().hasPermission("heavencraft.commands.modo"))
            update(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event)
    {
        // We watch only moderators
        if (event.getPlayer().hasPermission("heavencraft.commands.modo"))
            update(event.getPlayer().getName());
    }

    @EventHandler
    public void onChat(ChatEvent event)
    {
        if (event.isCancelled())
            return;

        // Console
        if (!(event.getSender() instanceof ProxiedPlayer))
            return;

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        // Normal player
        if (!player.hasPermission("heavencraft.commands.modo"))
            return;

        // WorldEdit command
        if (event.getMessage().startsWith("//"))
            return;

        if (player.getServer().getInfo().getName().equals("build"))
            return;

        update(player.getName());
    }
}