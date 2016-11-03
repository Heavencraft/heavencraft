package fr.heavencraft.heavenproxy.ban;

import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.commands.HeavenCommand;
import fr.heavencraft.heavenproxy.database.users.UserProvider;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;
import net.md_5.bungee.api.CommandSender;

public class BanCommand extends HeavenCommand
{
    public BanCommand()
    {
        super("ban", "heavencraft.commands.ban", new String[] { "gban" });
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) throws HeavenException
    {
        String playerName;
        String reason;

        switch (args.length)
        {
            case 0:
                sendUsage(sender);
                return;
            case 1:
                playerName = Utils.getRealName(args[0]);
                reason = "";
                break;
            default:
                playerName = Utils.getRealName(args[0]);
                reason = Utils.ArrayToString(args, 1, " ");
                break;
        }

        BanManager.banPlayer(UserProvider.getUserByName(playerName), reason, sender);
    }

    private static void sendUsage(CommandSender sender)
    {
        Utils.sendMessage(sender, "/{ban} <joueur>");
        Utils.sendMessage(sender, "/{ban} <joueur> <raison>");
    }
}