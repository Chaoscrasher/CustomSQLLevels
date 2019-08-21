package com.jb1services.mc.junkdudehd.customlevels.main;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
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
import com.jb1services.mc.junkdudehd.customlevels.commands.LevelCommand;
import com.jb1services.mc.junkdudehd.customlevels.db.MySQLDatabaseAccessObject;
import com.jb1services.mc.junkdudehd.customlevels.events.DailyRewardEvent;
import com.jb1services.mc.junkdudehd.customlevels.events.PlayerKillRewardEvent;
import com.jb1services.mc.junkdudehd.customlevels.events.PlayerMineReward;

	
public class CustomLevelsPlugin extends JavaPlugin {
	public static CustomLevelsPlugin instance;
	
	private MySQLDatabaseAccessObject dao;

	@Override
	public void onEnable()
	{
		instance = this;
		System.out.println("simpleloans loaded!");
		new LevelCommand(this);
		this.getServer().getPluginManager().registerEvents(new PlayerKillRewardEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new DailyRewardEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerMineReward(this), this);
		this.saveDefaultConfig();
		dao = new MySQLDatabaseAccessObject(getUser(),
				getPW(), 
				getTimeZone(),
				getAddress(),
				getDatabase());
	}
		
	@Override
	public void onDisable()
	{
		
	}
	
	public void executeSQL(String query)
	{
		//TODO: Implement
	}
   
	public String getUser()
	{
		return getConfig().getString("sql.user");
	}
	
	public String getPW()
	{
		return getConfig().getString("sql.pw");
	}
	
	public String getTimeZone()
	{
		return getConfig().getString("sql.timezone");
	}
	
	public String getAddress()
	{
		return getConfig().getString("sql.address");
	}
	
	public String getDatabase()
	{
		return getConfig().getString("sql.database");
	}
	
	public void setLastLoginRewardDate(UUID user, LocalDate time)
	{
    	String sqlDS = FormatUtil.jodaDateToSQLDateString(time);
		executeSQL("UPDATE TABLE users SET last_login_reward = '"+sqlDS+"' WHERE uuid = '"+user+"';");
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

	public Optional<Integer> getLevel(UUID player) throws SQLException
	{
	    Optional<Integer> axpo = dao.getXP(player);
	    if (axpo.isPresent())
	    {
	    	int axp = axpo.get();
		    double xpIncrease = getXPIncrease();
		    //xp at level x = l1t(20)*xpIncrease(1.5)^(x-1)
		    int cxp = getL1Threshold();
		    int lv = 1;
		    while (cxp <= axp)
		    {
		    	cxp *= xpIncrease;
		    	lv++;
		    }
		    return Optional.of(lv);
	    }
	    return Optional.empty();
	}
	
	public MySQLDatabaseAccessObject getDAO()
	{
		return this.dao;
	}
}