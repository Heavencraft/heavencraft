package fr.heavencraft.heavenproxy.ban;

import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.commands.HeavenCommand;
import fr.heavencraft.heavenproxy.database.users.UserProvider;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;
import net.md_5.bungee.api.CommandSender;

public class UnbanCommand extends HeavenCommand
{
    public UnbanCommand()
    {
        super("unban", "heavencraft.commands.unban", new String[] { "gunban" });
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) throws HeavenException
    {
        switch (args.length)
        {
            case 1:
                BanManager.unbanPlayer(UserProvider.getUserByName(args[0]), sender);
                break;
            default:
                sendUsage(sender);
                break;
        }
    }

    private void sendUsage(CommandSender sender)
    {
        Utils.sendMessage(sender, "/{unban} <joueur>");
    }
}