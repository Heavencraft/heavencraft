package fr.hc.core.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.hc.core.db.users.User;

public class FirstLoginEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final User user;

	public FirstLoginEvent(Player player, User user)
	{
		this.player = player;
		this.user = user;
	}

	public Player getPlayer()
	{
		return player;
	}

	public User getUser()
	{
		return user;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

}