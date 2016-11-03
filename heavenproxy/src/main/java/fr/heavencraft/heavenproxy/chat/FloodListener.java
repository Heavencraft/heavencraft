package fr.heavencraft.heavenproxy.chat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.heavencraft.heavenproxy.AbstractListener;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.ban.BanManager;
import fr.heavencraft.heavenproxy.database.users.UserProvider;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.exceptions.SQLErrorException;
import fr.heavencraft.heavenproxy.exceptions.UserNotFoundException;
import fr.heavencraft.heavenproxy.mute.MuteHelper;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.event.EventHandler;

public class FloodListener extends AbstractListener
{
    private final static String WARNING = "§d[de Prof. Chen]§r Arrête d'écrire le même message.";

    private final List<String> toIgnore = new ArrayList<String>();
    private final Map<String, String> _history = new HashMap<String, String>();
    private final Map<String, Calendar> _timestamps = new HashMap<String, Calendar>();
    private final Map<String, Integer> _counter = new HashMap<String, Integer>();

    public FloodListener()
    {
        // I'm chatting on my iPhone using Minecraft Connect! Check it out, it's free :)
        // I'm chatting on my iPhone using Minecraft Connect! Check it out, it's free :)
        // I'm chatting on my iPad using Minecraft Connect! Check it out, it's free :)
        // I'm chatting on my iPad using Minecraft Connect! Check it out, it's free :)
        // I'm chatting on my iPod touch using Minecraft Connect! Check it out, it's free :)
        // I'm chatting on my iPod touch using Minecraft Connect! Check it out, it's free :)
        toIgnore.add("minecraft connect");

        // I'm chatting on my iPhone using MC Connect for Minecraft! Check it out, it's free :)
        // I'm chatting on my iPad using MC Connect for Minecraft! Check it out, it's free :)
        // I'm chatting on my iPod touch using MC Connect for Minecraft! Check it out, it's free :)
        toIgnore.add("mc connect");

        // connected with an iPhone using MineChat
        // connected with an iPad using MineChat
        // connected with an iPod touch using MineChat
        toIgnore.add("minechat");

        // Connected via "MC Chat" for Android!
        toIgnore.add("mc chat");

        // Connected from Hyperchat v2.6.04 on a Samsung GT-I9505
        // Connected from Hyperchat v2.6.07 on a HTC One S
        toIgnore.add("hyperchat");
        
        // Connected using PickaxeChat for Android
        toIgnore.add("PickaxeChat");
        
        // I joined from Android using ChatCraft for Minecraft! You can access premium servers freely
        toIgnore.add("ChatCraft");
        
    }

    @EventHandler
    public void onChat(ChatEvent event)
    {
        if (event.isCancelled())
            return;

        if (!(event.getSender() instanceof ProxiedPlayer))
            return;

        final String message = event.getMessage().toLowerCase();

        // Si c'est une commande autre que /m, /me ou /send -> on fait rien
        if (event.isCommand() && !message.startsWith("/m ") && !message.startsWith("/msg ")
                && !message.startsWith("/t ") && !message.startsWith("/tell ") && !message.startsWith("/w ")
                && !message.startsWith("/me ") && !message.startsWith("/envoyer "))
            return;

        // Si c'est un des messages à filter -> on annule le message
        for (final String keyword : toIgnore)
        {
            if (message.contains(keyword))
            {
                event.setCancelled(true);
                return;
            }
        }

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        final String playerName = player.getName();

        // BUGFIX : Si c'est pour la banque Semi-RP -> on fait rien
        if (player.getServer().getInfo().getName().equalsIgnoreCase("semirp") && Utils.isInteger(message))
        {
            return;
        }

        final String lastMessage = _history.get(playerName);
        final Calendar lastTimestamp = _timestamps.get(playerName);

        if (lastTimestamp != null)
            lastTimestamp.add(Calendar.SECOND, 15);

        if (lastMessage != null && lastMessage.equals(message) && lastTimestamp.after(Calendar.getInstance()))
        {
            event.setCancelled(true);

            final Integer counter = _counter.get(playerName) + 1;
            _counter.put(playerName, counter);

            switch (counter)
            {
                case 1:
                case 2:
                    // KickCommand.kickPlayer(player, "le Prof. Chen",
                    // "Ce n'est pas le moment de flooder !");
                    Utils.sendMessage(player, WARNING);
                    break;
                case 3:
                    try
                    {
                        MuteHelper.mute(UserProvider.getUserByUniqueId(player.getUniqueId()), 3);
                    }
                    catch (final HeavenException ex)
                    {
                        log.error("HeavenException when trying to mute %1$s", playerName);
                        ex.printStackTrace();
                    }
                    break;
                case 4:
                    Utils.sendMessage(player, WARNING);
                    Utils.sendMessage(player,
                            "Ceci est le 4e avertissement pour flood, au prochain, ce sera un ban. Merci d'arrêter.");
                    break;
                case 5:
                    try
                    {
                        BanManager.banPlayer(UserProvider.getUserByUniqueId(player.getUniqueId()),
                                String.format("Flood abusif : '%1$s'", message));
                    }
                    catch (UserNotFoundException | SQLErrorException ex)
                    {
                        ex.printStackTrace();
                    }
                    break;
            }
        }
        else
        {
            _history.put(playerName, message);
            _counter.put(playerName, 0);
        }

        _timestamps.put(playerName, Calendar.getInstance());
    }
}
