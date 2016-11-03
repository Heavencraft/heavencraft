package fr.heavencraft.heavenproxy.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DisconnectReasonManager
{
	private static final String RAGEQUIT = "R";
	private static final String KICK = "K|%1$s|%2$s";
	private static final String BAN = "B|%1$s|%2$s";

	private static final Map<String, String> _reasons = new ConcurrentHashMap<String, String>();

	public static void addRagequit(final String playerName)
	{
		addReason(playerName, RAGEQUIT);
	}

	public static void addKick(final String playerName, final String kickedBy, final String reason)
	{
		addReason(playerName, String.format(KICK, kickedBy, reason));
	}

	public static void addBan(final String playerName, final String bannedBy, final String reason)
	{
		addReason(playerName, String.format(BAN, bannedBy, reason));
	}

	private static void addReason(final String playerName, final String reason)
	{
		_reasons.put(playerName, reason);
	}

	public static String getReason(final String playerName)
	{
		return _reasons.remove(playerName);
	}
}