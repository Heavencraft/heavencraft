package fr.hc.core;

public class BukkitHeavenCore extends AbstractBukkitPlugin implements HeavenCore
{
	public BukkitHeavenCore()
	{
		HeavenCoreInstance.set(this);
	}
}