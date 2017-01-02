package fr.hc.core.horses;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import fr.hc.core.CorePermissions;
import fr.hc.core.utils.chat.ChatUtil;

public class HorsesManager
{
	public static boolean isWild(AbstractHorse horse)
	{
		return !horse.isTamed();
	}

	public static boolean canUse(AbstractHorse horse, Player player)
	{
		if (player.hasPermission(CorePermissions.HORSE_BYPASS))
			return true;
		else if (isWild(horse))
			return true;
		else if (horse.getOwner() == null)
		{
			horse.setOwner(player);
			ChatUtil.sendMessage(player, "Vous êtes désormais le propriétaire de ce cheval.");
			return true;
		}
		else
			return horse.getOwner().getUniqueId().equals(player.getUniqueId());
	}

	public static void sendWarning(AbstractHorse horse, Player player)
	{
		if (horse.getOwner() == null)
			ChatUtil.sendMessage(player, "Ce cheval est apprivoisé mais n'a pas de propriétaire. Merci de contacter un administrateur.");
		else
			ChatUtil.sendMessage(player, "Ce cheval appartient à {%1$s}.", horse.getOwner().getName());
	}
}