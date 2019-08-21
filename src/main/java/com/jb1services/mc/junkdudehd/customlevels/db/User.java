package com.jb1services.mc.junkdudehd.customlevels.db;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class User
{
	private UUID uuid;
	private int xp;
	private LocalDate last_reward_date;
	
	public User(UUID uuid, int xp, LocalDate last_reward_date)
	{
		super();
		this.uuid = uuid;
		this.xp = xp;
		this.last_reward_date = last_reward_date;
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public int getXp()
	{
		return xp;
	}

	public LocalDate getLast_reward_date()
	{
		return last_reward_date;
	}
}
