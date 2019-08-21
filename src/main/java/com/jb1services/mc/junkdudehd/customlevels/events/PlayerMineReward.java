package com.jb1services.mc.junkdudehd.customlevels.events;

import java.sql.SQLException;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.jb1services.mc.junkdudehd.customlevels.main.CustomLevelsPlugin;

public class PlayerMineReward implements Listener
{
	private CustomLevelsPlugin plugin;
	
	public PlayerMineReward(CustomLevelsPlugin plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent e)
	{
		if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			try
			{
				plugin.getDAO().addXP(e.getPlayer().getUniqueId(), plugin.getMineXP());
			} catch (SQLException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
}
