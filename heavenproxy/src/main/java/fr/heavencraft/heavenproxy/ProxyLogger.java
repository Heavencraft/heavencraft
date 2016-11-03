package fr.heavencraft.heavenproxy;

public class ProxyLogger
{
	private final String prefix;

	private ProxyLogger(String prefix)
	{
		this.prefix = prefix;
	}

	public void info(String format, Object... args)
	{
		HeavenProxy.getInstance().getLogger().info(prefix + String.format(format, args));
	}

	public void warm(String format, Object... args)
	{
		HeavenProxy.getInstance().getLogger().warning(prefix + String.format(format, args));
	}

	public void error(String format, Object... args)
	{
		HeavenProxy.getInstance().getLogger().severe(prefix + String.format(format, args));
	}

	public static ProxyLogger getLogger(Class<?> clazz)
	{
		return new ProxyLogger(clazz.getSimpleName());
	}
}