package com.jb1services.mc.muttley.simpleloans.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.jb1services.mc.muttley.simpleloans.main.CustomLevelsPlugin;

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
			plugin.addXP(e.getPlayer().getUniqueId(), plugin.getMineXP());
	}
}
