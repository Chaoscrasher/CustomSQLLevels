package com.jb1services.mc.muttley.simpleloans.commands;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.joda.time.DateTime;


import com.jb1services.mc.muttley.simpleloans.main.CustomLevelsPlugin;


import net.md_5.bungee.api.ChatColor;

public class LoanCommand implements CommandExecutor {

	public static boolean debug1 = false;
	
	CustomLevelsPlugin slp;
	
	public LoanCommand(CustomLevelsPlugin slp)
	{
		this.slp = slp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
