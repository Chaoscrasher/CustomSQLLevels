package com.jb1services.mc.junkdudehd.customlevels.commands;

import java.sql.SQLException;
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

import com.jb1services.mc.junkdudehd.customlevels.main.CustomLevelsPlugin;

import net.md_5.bungee.api.ChatColor;

public class LevelCommand implements CommandExecutor {

	public static boolean debug1 = false;
	
	CustomLevelsPlugin plugin;
	
	public LevelCommand(CustomLevelsPlugin slp)
	{
		this.plugin = slp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (args.length == 0)
			{
				try
				{
					Optional<Integer> lvo = plugin.getLevel(player.getUniqueId());
					if (lvo.isPresent())
						sender.sendMessage("Your level is: " + lvo.get());
					else
						sender.sendMessage("Sorry, your XP data could not be retriveved from the server. Please report this to the server owner.");
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (args.length == 1)
			{
				if (sender.isOp())
				{
					if (args[0].equalsIgnoreCase("test-connection"))
					{
						System.out.println("Testing connection with user: '" + plugin.getUser() + "', timezone: '" + plugin.getTimeZone() + "', address: '" + plugin.getAddress() + "' & database: '" + plugin.getDatabase() + "'!");
						Optional<String> erroro = plugin.getDAO().testConnect();
						if (!erroro.isPresent())
							sender.sendMessage("Success!");
						else
							sender.sendMessage("Connection test failed due to: " + erroro.get());
					}
					else
					{
						sender.sendMessage("Unknown subcommand of levels '" + args[0] + "'! Valid is: 'test-connection' !");
					}
				}
			}
			else if (args.length == 2)
			{
				if (sender.isOp())
				{
					if (args[0].equalsIgnoreCase("test-connection"))
					{
						if (args[1].equalsIgnoreCase("testpw@5"))
						{
							System.out.println("Testing connection with user: '" + plugin.getUser() + "', password: '"+plugin.getPW()+"', timezone: '" + plugin.getTimeZone() + "', address: '" + plugin.getAddress() + "' & database: '" + plugin.getDatabase() + "'!");
							Optional<String> erroro = plugin.getDAO().testConnect();
							if (!erroro.isPresent())
								sender.sendMessage("Success!");
							else
								sender.sendMessage("Connection test failed due to: " + erroro.get());
						}
						else
							sender.sendMessage("Wrong password!");
					}
					else
					{
						sender.sendMessage("Unknown subcommand of levels '" + args[0] + "'! Valid is: 'test-connection' !");
					}
				}
			}
			else
				return false;
		}
		else
			sender.sendMessage(ChatColor.RED + "Only players can do this!");
		return true;
	}
}
