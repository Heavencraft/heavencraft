package fr.heavencraft.heavenproxy.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.database.users.User;
import fr.heavencraft.heavenproxy.database.users.UserProvider;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.exceptions.UserNotFoundException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ListCommand extends HeavenCommand
{
    private final static String LIST_MESSAGE = "Il y a {%1$d} joueurs connectés :";

    // private String allServers;

    public ListCommand()
    {
        super("list", "", new String[] { "online", "who", "glist" });
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) throws HeavenException
    {
        if (args.length == 1)
            listServer(sender, args[0]);
        else
            listAll(sender);
    }

    private void listAll(CommandSender sender) throws HeavenException
    {
        sendList(sender, ProxyServer.getInstance().getPlayers());
    }

    private void listServer(CommandSender sender, String serverName) throws HeavenException
    {
        final ServerInfo server = ProxyServer.getInstance().getServerInfo(serverName);

        if (server == null)
            listAll(sender);
        else
            sendList(sender, server.getPlayers());
    }

    private static void sendList(CommandSender sender, Collection<ProxiedPlayer> players) throws HeavenException
    {
        if (players.isEmpty())
        {
            Utils.sendMessage(sender, "Il n'y a personne ici...");
            return;
        }

        final List<String> names = new ArrayList<String>();

        for (final ProxiedPlayer player : players)
            names.add(player.getName());

        Collections.sort(names, new Comparator<String>()
        {
            @Override
            public int compare(String p1, String p2)
            {
                return p1.compareToIgnoreCase(p2);
            }
        });

        String list = "";

        for (String name : names)
        {
            try
            {
                final User user = UserProvider.getUserByName(name);

                if (!ChatColor.WHITE.toString().equals(user.getColor()))
                    name = user.getColor() + name + ChatColor.GOLD;
            }
            catch (final UserNotFoundException ex)
            {
            }

            list += (list.isEmpty() ? "" : ", ") + name;
        }

        Utils.sendMessage(sender, LIST_MESSAGE, players.size());
        Utils.sendMessage(sender, list);
    }
}