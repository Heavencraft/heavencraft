package fr.hc.rp;

public class HeavenRPInstance
{
	private static HeavenRP instance;

	public static HeavenRP get()
	{
		return instance;
	}

	public static void set(HeavenRP instance)
	{
		HeavenRPInstance.instance = instance;
	}
}