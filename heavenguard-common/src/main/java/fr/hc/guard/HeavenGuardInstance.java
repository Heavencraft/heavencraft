package fr.hc.guard;

public class HeavenGuardInstance
{
	private static HeavenGuard instance;

	public static HeavenGuard get()
	{
		return instance;
	}

	public static void set(HeavenGuard instance)
	{
		HeavenGuardInstance.instance = instance;
	}
}