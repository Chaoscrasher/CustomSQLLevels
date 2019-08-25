package com.jb1services.mc.junkdudehd.customlevels.events;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.jb1services.chaosutil.util.formating.FormatUtil;
import com.jb1services.mc.junkdudehd.customlevels.main.CustomLevelsPlugin;

public class DailyRewardEvent implements Listener
{
	private CustomLevelsPlugin plugin;
	
	public DailyRewardEvent(CustomLevelsPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handleDailyReward(PlayerJoinEvent e)
	{
	    Optional<LocalDate> odt;
		try
		{
			odt = plugin.getDAO().getLastRewardDate(e.getPlayer().getUniqueId());
			UUID user = e.getPlayer().getUniqueId();
		    if (odt.isPresent())
		    {
		        LocalDate dt = odt.get();
		        if (daysBetween(dt, DateTime.now()) > 1)
		        {
		            try
					{
						plugin.getDAO().addXP(user, plugin.getDailyRewardXP());
						plugin.getDAO().updateLastLoginRewardDateForUser(user);
					} catch (SQLException e1)
					{
						e1.printStackTrace();
					}
		        }
		    }
		    else
		    {
		    	plugin.getDAO().setUpUser(user);
		        plugin.getDAO().updateLastLoginRewardDateForUser(user);
		    }
		} catch (SQLException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	public int daysBetween(LocalDate a, DateTime b)
	{
		return Days.daysBetween(a, b.toLocalDate()).getDays();
	}
}
