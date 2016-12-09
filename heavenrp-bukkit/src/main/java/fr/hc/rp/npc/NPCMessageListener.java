package fr.hc.rp.npc;

import org.bukkit.event.EventHandler;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.rp.BukkitHeavenRP;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class NPCMessageListener extends AbstractBukkitListener
{
	public NPCMessageListener(BukkitHeavenRP plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onNPCRightClick(NPCRightClickEvent event)
	{
	}
}