package fr.hc.rp.cmd.parcelle;

import fr.hc.guard.HeavenGuard;
import fr.hc.guard.HeavenGuardInstance;
import fr.hc.guard.db.regions.Region;

public class ParcelleCommandUtil
{
	private static final HeavenGuard guard = HeavenGuardInstance.get();

	public static String createRegionName(Region parent, String userName)
	{
		String regionName;
		int i = 1;
		do
		{
			regionName = parent.getName() + "_" + userName + "_" + i++;
		}
		while (guard.getRegionProvider().regionExists(regionName));

		return regionName;
	}
}