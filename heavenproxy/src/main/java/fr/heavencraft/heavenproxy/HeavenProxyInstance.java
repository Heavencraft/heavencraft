package fr.heavencraft.heavenproxy;

public class HeavenProxyInstance
{
	private static HeavenProxy instance;

	public static HeavenProxy get()
	{
		return instance;
	}

	public static void set(HeavenProxy instance)
	{
		HeavenProxyInstance.instance = instance;
	}
}