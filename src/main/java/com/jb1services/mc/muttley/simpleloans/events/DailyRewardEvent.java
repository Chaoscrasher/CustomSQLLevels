package com.jb1services.mc.muttley.simpleloans.events;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.jb1services.chaosutil.util.formating.FormatUtil;
import com.jb1services.mc.muttley.simpleloans.main.CustomLevelsPlugin;

public class DailyRewardEvent
{
	private CustomLevelsPlugin plugin;
	
	public DailyRewardEvent(CustomLevelsPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handleDailyReward(PlayerJoinEvent e)
	{
	    Optional<DateTime> odt = plugin.getLastRewardDate(e.getPlayer().getUniqueId());
	    UUID user = e.getPlayer().getUniqueId();
	    if (odt.isPresent())
	    {
	        DateTime dt = odt.get();
	        if (daysBetween(dt, DateTime.now()) > 1)
	        {
	            plugin.addXP(user, plugin.getDailyRewardXP());
	            plugin.setLastLoginRewardDate(user, DateTime.now().toLocalDate());
	        }
	    }
	    else
	    {
	        plugin.setLastLoginRewardDate(user, DateTime.now().toLocalDate());

	        String sqlDate = FormatUtil.jodaDateToSQLDateString(DateTime.now().toLocalDate());
	    	plugin.executeSQL("UPDATE TABLE users SET last_login = '"+sqlDate+"' where uuid = '"+user+"';");
	    }
	}
	
	public int daysBetween(DateTime a, DateTime b)
	{
		return Days.daysBetween(a.toLocalDate(), b.toLocalDate()).getDays();
	}
}
