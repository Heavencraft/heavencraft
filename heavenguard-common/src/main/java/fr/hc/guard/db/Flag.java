package fr.hc.guard.db;

import java.util.HashMap;
import java.util.Map;

public class Flag
{
	private static Map<String, Flag> flagsByName = new HashMap<String, Flag>();

	public static final Flag PVP = new Flag("pvp", FlagType.BOOLEAN);
	public static final Flag PUBLIC = new Flag("public", FlagType.BOOLEAN);
	public static final Flag REMOVE_TIMESTAMP = new Flag("remove_timestamp", FlagType.TIMESTAMP);
	public static final Flag STATE = new Flag("state", FlagType.BYTE_ARRAY);

	public static Flag getUniqueInstanceByName(String name)
	{
		return flagsByName.get(name.toLowerCase());
	}

	private final String name;
	private final FlagType type;

	Flag(String name, FlagType type)
	{
		this.name = name;
		this.type = type;

		flagsByName.put(name, this);
	}

	public String getName()
	{
		return name;
	}

	public FlagType getType()
	{
		return type;
	}
}