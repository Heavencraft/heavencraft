package fr.hc.core;

public class HeavenCoreInstance
{
	private static HeavenCore instance;

	public static HeavenCore get()
	{
		return instance;
	}

	public static void set(HeavenCore instance)
	{
		HeavenCoreInstance.instance = instance;
	}
}