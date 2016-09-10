package fr.hc.guard;

import org.bukkit.Bukkit;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.User;
import fr.hc.core.db.users.UserProvider;
import fr.hc.guard.db.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.listeners.PlayerListener;
import fr.heavencraft.heavenguard.bukkit.listeners.ProtectionEnvironmentListener;
import fr.heavencraft.heavenguard.bukkit.listeners.ProtectionPlayerListener;

/*
 * Database looks like :
 * regions (
 *   id, name, parent_id,
 *   world, min_x, min_y, min_z, max_x, max_y, max_z,
 *   ... +flags
 * )
 * 
 * hg_regions_members (region_id, user_id, owner)
 * 
 * hg_uuid (id, uuid, last_name)
 * 
 */
public class BukkitHeavenGuard extends AbstractBukkitPlugin implements HeavenGuard
{
	private RegionProvider regionProvider;
	private RegionManager regionManager;

	private ConnectionProvider connectionProvider;

	public BukkitHeavenGuard()
	{
		HeavenGuardInstance.set(this);
	}

	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();
			saveDefaultConfig();

			connectionProvider = createConnectionProvider(getConfig());
			initDatabaseIfNeeded(connectionProvider, "SELECT * FROM users LIMIT 1");

			new PlayerListener(this);

			new ProtectionPlayerListener(this);
			new ProtectionEnvironmentListener(this);

			regionProvider = new RegionProvider(connectionProvider);
			regionManager = new RegionManager(regionProvider);

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

	public RegionManager getRegionManager()
	{
		return regionManager;
	}

	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
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