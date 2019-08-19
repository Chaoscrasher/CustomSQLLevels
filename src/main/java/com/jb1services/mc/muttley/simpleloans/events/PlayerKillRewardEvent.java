package com.jb1services.mc.muttley.simpleloans.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.jb1services.mc.muttley.simpleloans.main.CustomLevelsPlugin;

public class PlayerKillRewardEvent implements Listener
{
	CustomLevelsPlugin plugin;
	
	public PlayerKillRewardEvent(CustomLevelsPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		LivingEntity entity = e.getEntity().getKiller();
		if (entity != null && entity instanceof Player)
		{
			Player killer = (Player) entity;
			plugin.addXP(killer.getUniqueId(), plugin.getPlayerKillXP());
		}
	}
}
