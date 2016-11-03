package fr.heavencraft.heavenproxy.mute;

import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.commands.HeavenCommand;
import fr.heavencraft.heavenproxy.database.users.User;
import fr.heavencraft.heavenproxy.database.users.UserProvider;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;
import net.md_5.bungee.api.CommandSender;

public class MuteCommand extends HeavenCommand
{
	public static String permission = "heavencraft.commands.mute";
    public MuteCommand()
    {
        super("mute", MuteCommand.permission, new String[] {});
    }

    @Override
    protected void onCommand(final CommandSender sender, String[] args) throws HeavenException
    {
        if (args.length != 2)
        {
            sendUsage(sender);
            return;
        }

        final String playerName = Utils.getRealName(args[0]);

        final User user = UserProvider.getUserByName(playerName);
        final int nbMinutes = Utils.toUint(args[1]);

        MuteHelper.mute(user, nbMinutes, sender);
    }

    private static void sendUsage(CommandSender sender)
    {
        Utils.sendMessage(sender, "/{mute} <joueur> <temps en minutes>");
    }
}