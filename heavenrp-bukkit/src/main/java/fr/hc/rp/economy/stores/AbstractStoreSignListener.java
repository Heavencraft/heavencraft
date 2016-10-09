package fr.hc.rp.economy.stores;

import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.AbstractSignListener;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;

public abstract class AbstractStoreSignListener extends AbstractSignListener
{
	protected static final int COMPANY_LINE = 1;
	protected static final int NAME_LINE = 2;
	protected static final int QUANTITY_PRICE_LINE = 3;
	protected static final char QUANTITY_PRICE_SEPARATOR = '@';

	protected final HeavenRP plugin = HeavenRPInstance.get();

	public AbstractStoreSignListener(JavaPlugin plugin, String tag, String permission)
	{
		super(plugin, tag, permission);
	}
}