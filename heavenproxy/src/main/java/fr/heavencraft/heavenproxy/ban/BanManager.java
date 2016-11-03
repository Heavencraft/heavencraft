package fr.heavencraft.heavenproxy.ban;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.async.QueriesHandler;
import fr.heavencraft.heavenproxy.chat.DisconnectReasonManager;
import fr.heavencraft.heavenproxy.database.banlist.BanQuery;
import fr.heavencraft.heavenproxy.database.banlist.UnbanQuery;
import fr.heavencraft.heavenproxy.database.users.User;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.exceptions.PlayerNotConnectedException;
import fr.heavencraft.heavenproxy.listeners.LogListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanManager
{
    private final static String KICK_REASON = "Vous avez été banni du serveur par %1$s :\n\n%2$s";
    private final static String BAN_MESSAGE = "Le joueur {%1$s} a été banni d'Heavencraft.";
    private final static String UNBAN_MESSAGE = "Le joueur {%1$s} a été débanni d'Heavencraft.";

    // Used by /ban command
    public static void banPlayer(User user, String reason, CommandSender sender)
    {
        banPlayer(user, sender.getName(), reason, sender);
    }

    // Used by plugin
    public static void banPlayer(User user, String reason)
    {
        banPlayer(user, "le Prof. Chen", reason, null);
    }

    private static void banPlayer(final User user, final String bannedBy, final String reason,
            final CommandSender sender)
    {
        QueriesHandler.addQuery(new BanQuery(user, bannedBy, reason)
        {
            @Override
            public void onSuccess()
            {
                final String playerName = user.getName();

                LogListener.addBan(playerName, bannedBy, reason);
                if (sender != null)
                    Utils.sendMessage(sender, BAN_MESSAGE, playerName);

                try
                {
                    final ProxiedPlayer player = Utils.getPlayer(playerName);

                    DisconnectReasonManager.addBan(player.getName(), bannedBy, reason);
                    Utils.kickPlayer(player, String.format(KICK_REASON, bannedBy, reason));
                }
                catch (final PlayerNotConnectedException ex)
                {
                }
            }
        });
    }

    public static void unbanPlayer(final User user, final CommandSender sender) throws HeavenException
    {
        QueriesHandler.addQuery(new UnbanQuery(user)
        {
            @Override
            public void onSuccess()
            {
                LogListener.addUnban(user.getName(), sender.getName());
                Utils.sendMessage(sender, UNBAN_MESSAGE, user.getName());
            }
        });
    }

    public static String getReason(UUID uniqueId)
    {
        try
        {
            final PreparedStatement ps = HeavenProxy.getConnection()
                    .prepareStatement("SELECT CONCAT_WS(' : ', banned_by, reason) FROM banlist WHERE uuid = ?;");
            ps.setString(1, uniqueId.toString());

            final ResultSet rs = ps.executeQuery();

            if (!rs.next())
                return null;

            return rs.getString(1);
        }
        catch (final SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
