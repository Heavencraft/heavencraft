package fr.hc.rp.warps;

import java.util.List;
import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.RPPermissions;
import fr.hc.rp.db.users.RPUser;
import fr.hc.rp.db.warps.RPWarp;
import fr.hc.rp.db.warps.RPWarpProvider;

public class WarpCommandExecutor extends AbstractCommandExecutor
{

	// private static final String WarpPermission = "heavencraft.commands.warp";
	private final HeavenRP plugin = HeavenRPInstance.get();

	private static final String WARP_DOES_NOT_EXIST = "Le warp {%1$s} n'existe pas.";
	private static final String WARP_DOES_ALREADY_EXIST = "Le warp {%1$s} existe déjà.";

	public WarpCommandExecutor(BukkitHeavenRP plugin)
	{
		super(plugin, "warp", RPPermissions.WARP_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		RPWarpProvider warpProvider = plugin.getWarpProvider();
		Optional<RPUser> user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());

		// Parse the command
		switch (args.length)
		{
			case 1:
				if (args[0].equalsIgnoreCase("list"))
				{
					List<RPWarp> warps = warpProvider.listWarps();
					String list = "";

					// Build the list
					for (RPWarp warp : warps)
					{
						list += (list == "" ? "" : ", ") + "{" + warp.getName() + "}";
					}

					ChatUtil.sendMessage(player, "Liste des warps :");
					ChatUtil.sendMessage(player, list);
					return;
				}

				sendUsage(player);
				break;

			case 2:

				// Does the warp exist?
				Optional<RPWarp> wrp = plugin.getWarpProvider().getWarpByName(args[1]);

				switch (args[0].toLowerCase())
				{
					case "define":
						// Does the warp exist?
						if (wrp.isPresent())
						{
							ChatUtil.sendMessage(player, WARP_DOES_ALREADY_EXIST, args[1]);
							return;
						}
						warpProvider.createWarp(args[1], user.get(), player.getLocation().getWorld().getName(),
								player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
								player.getLocation().getYaw(), player.getLocation().getPitch());
						ChatUtil.sendMessage(player, "Le warp {%1$s} a bien été défini.", args[1]);
						return;

					case "remove":
						// Does the warp exist?
						if (!wrp.isPresent())
						{
							ChatUtil.sendMessage(player, WARP_DOES_NOT_EXIST, args[1]);
							return;
						}

						warpProvider.deleteWarp(args[1]);
						ChatUtil.sendMessage(player, "Le warp {%1$s} a bien été supprimé.", args[1]);
						return;
					case "tp":
						// Does the warp exist?
						if (!wrp.isPresent())
						{
							ChatUtil.sendMessage(player, WARP_DOES_NOT_EXIST, args[1]);
							return;
						}
						player.teleport(WarpUtils.getLocation(wrp.get()));
						ChatUtil.sendMessage(player, "Vous avez été téléporté à {%1$s}.", args[1]);
						return;
				}

				break;
			default:
				sendUsage(player);
				return;
		}

	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		throw new HeavenException("Uniquement utilisable depuis le jeu.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{warp} define <nom>");
		ChatUtil.sendMessage(sender, "/{warp} list");
		ChatUtil.sendMessage(sender, "/{warp} tp <nom>");
		ChatUtil.sendMessage(sender, "/{warp} remove <nom>");
	}

}
