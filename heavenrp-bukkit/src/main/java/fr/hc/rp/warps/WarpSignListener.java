package fr.hc.rp.warps;

import java.util.Optional;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.hc.core.AbstractSignListener;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.warps.RPWarp;

public class WarpSignListener extends AbstractSignListener
{

	private final HeavenRP plugin = HeavenRPInstance.get();

	public WarpSignListener(BukkitHeavenRP plugin)
	{
		super(plugin, "Warp", "heavencraft.signs.warps");
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		System.out.println("Placed");
		Optional<RPWarp> wrp = plugin.getWarpProvider().getWarpByName(event.getLine(1));
		return wrp.isPresent();
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		System.out.println("Clicked");
		Optional<RPWarp> wrp = plugin.getWarpProvider().getWarpByName(sign.getLine(1));
		if (wrp.isPresent())
		{
			PlayerUtil.teleportPlayer(player, WarpUtils.getLocation(wrp.get()));
		}
	}

}
