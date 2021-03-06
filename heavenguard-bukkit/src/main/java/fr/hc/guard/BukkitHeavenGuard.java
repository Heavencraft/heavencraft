package fr.hc.guard;

import org.bukkit.Bukkit;

import fr.hc.core.AbstractDatabaseBukkitPlugin;
import fr.hc.core.db.users.User;
import fr.hc.core.db.users.UserProvider;
import fr.hc.guard.db.GlobalRegionProvider;
import fr.hc.guard.db.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.listeners.PlayerListener;
import fr.heavencraft.heavenguard.bukkit.listeners.ProtectionEnvironmentListener;
import fr.heavencraft.heavenguard.bukkit.listeners.ProtectionPlayerListener;
import fr.heavencraft.heavenguard.bukkit.listeners.TeleportFlagListener;

public class BukkitHeavenGuard extends AbstractDatabaseBukkitPlugin implements HeavenGuard
{
	private RegionProvider regionProvider;
	private GlobalRegionProvider globalRegionProvider;
	private RegionManager regionManager;

	public BukkitHeavenGuard()
	{
		super("SELECT * FROM regions LIMIT 1");
		HeavenGuardInstance.set(this);
	}

	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();
			saveDefaultConfig();

			new PlayerListener(this);

			new ProtectionPlayerListener(this);
			new ProtectionEnvironmentListener(this);
			new TeleportFlagListener(this);

			regionProvider = new RegionProvider(connectionProvider);
			globalRegionProvider = new GlobalRegionProvider(connectionProvider);
			regionManager = new RegionManager(regionProvider, globalRegionProvider);

			new RegionCommand(this);
			new RemoveRegionTask(this);
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	public RegionProvider getRegionProvider()
	{
		return regionProvider;
	}

	@Override
	public GlobalRegionProvider getGlobalRegionProvider()
	{
		return globalRegionProvider;
	}

	@Override
	public RegionManager getRegionManager()
	{
		return regionManager;
	}

	private UserProvider<? extends User> userProvider;

	@Override
	public UserProvider<? extends User> getUserProvider()
	{
		return userProvider;
	}

	@Override
	public void setUserProvider(UserProvider<? extends User> userProvider)
	{
		this.userProvider = userProvider;
	}
}