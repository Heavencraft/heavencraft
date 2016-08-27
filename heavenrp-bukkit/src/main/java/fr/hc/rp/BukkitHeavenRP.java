package fr.hc.rp;

import fr.hc.core.AbstractBukkitPlugin;

public class BukkitHeavenRP extends AbstractBukkitPlugin implements HeavenRP
{
	public BukkitHeavenRP()
	{
		HeavenRPInstance.set(this);
	}
}