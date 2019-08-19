package com.jb1services.mc.muttley.simpleloans.main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.jb1services.chaosutil.util.formating.FormatUtil;
import com.jb1services.chaosutil.util.ui.IOUtil;
import com.jb1services.mc.muttley.simpleloans.commands.LoanCommand;
import com.jb1services.mc.muttley.simpleloans.events.TestEvents;

	
public class CustomLevelsPlugin extends JavaPlugin {
	public static CustomLevelsPlugin instance;

	@Override
	public void onEnable()
	{
		instance = this;
		System.out.println("simpleloans loaded!");
		new LoanCommand(this);
		this.saveDefaultConfig();
	}
		
	@Override
	public void onDisable()
	{
		
	}
	
	public void executeSQL(String query)
	{
		//TODO: Implement
	}
	
	public void addXP(UUID user, int xpToAdd)
	{
		int cxp = getXP(user);
		executeSQL("UPDATE TABLE users SET xp = "+(cxp+xpToAdd)+" where uuid = '"+user+"';");
	}

	public int getXP(UUID user)
	{
		executeSQL("SELECT xp FROM TABLE users WHERE uuid = '"+user+"';");
	}
	
	public Optional<DateTime> getDateTimeFromSQL(String sql)
	{
		return Optional.empty();
	}
	
	public	Optional<DateTime> getLastRewardDate(UUID user)
	{
		String sql = "SELECT last_reward_date FROM TABLE users WHERE uuid = '"+user+"';";
		return getDateTimeFromSQL(sql);
	}
   

	public void setLastLoginRewardDate(UUID user, LocalDate time)
	{
    	String sqlDS = FormatUtil.jodaDateToSQLDateString(time);
		executeSQL("UPDATE TABLE users SET date = '"+sqlDS+"' WHERE uuid = '"+user+"';");
	}

//default value?
	public int getDailyRewardXP()
	{
		return getConfig().getInt("rewards.daily-login");
	}

	//default value?
	public int getPlayerKillXP()
	{
		return getConfig().getInt("rewards.player-kill");
	}

//default value?
	public int getMineXP()
	{
		return getConfig().getInt("rewards.block-mined");
	}

	public int getL1Threshold()
	{
		return getConfig().getInt("l1-threshold");
	}

	public double getXPIncrease()
	{
    	return getConfig().getDouble("xpIncrease");
	}

	public int getLevel(UUID player)
	{
	    int axp = getXP(player);
	    double xpIncrease = getXPIncrease();
	    //xp at level x = l1t(20)*xpIncrease(1.5)^(x-1)
	    int cxp = getL1Threshold();
	    int lv = 1;
	    while (cxp <= axp)
	    {
	    	cxp *= xpIncrease;
	    	lv++;
	    }
	    return lv;
	}

}