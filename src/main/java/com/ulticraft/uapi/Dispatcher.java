package com.ulticraft.uapi;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Dispatcher
{
	private Plugin pl;

	public Dispatcher(Plugin pl)
	{
		this.pl = pl;
	}

	public void msg(CommandSender sender, String... o)
	{
		String msg = "";

		for(String i : o)
		{
			msg = msg + i;
		}

		try
		{
			sender.sendMessage(msg);
		}

		catch (NullPointerException e)
		{
			return;
		}
	}

	private void log(String s, String... o)
	{
		String msg = s + "";

		for(String i : o)
		{
			msg = msg + i;
		}

		pl.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + pl.getName() + ": " + msg);
	}
	
	public void message(CommandSender sender, String tag, String msg)
	{
		sender.sendMessage(tag + msg);
	}
	
	public void message(Player sender, String tag, String msg)
	{
		sender.sendMessage(tag + msg);
	}
	
	public void message(CommandSender sender, String msg)
	{
		sender.sendMessage(msg);
	}
	
	public void message(Player sender, String msg)
	{
		sender.sendMessage(msg);
	}

	public void info(String... o)
	{
		log("" + ChatColor.WHITE, o);
	}

	public void success(String... o)
	{
		log("" + ChatColor.GREEN, o);
	}

	public void failure(String... o)
	{
		log("" + ChatColor.RED, o);
	}

	public void warning(String... o)
	{
		log("" + ChatColor.YELLOW, o);
	}

	public void verbose(String... o)
	{
		log("" + ChatColor.LIGHT_PURPLE, o);
	}

	public void overbose(String... o)
	{
		log("" + ChatColor.AQUA, o);
	}

	public void sinfo(String... o)
	{
		log("" + ChatColor.GRAY, o);
	}

	public void ssuccess(String... o)
	{
		log("" + ChatColor.DARK_GREEN, o);
	}

	public void sfailure(String... o)
	{
		log("" + ChatColor.DARK_RED, o);
	}

	public void swarning(String... o)
	{
		log("" + ChatColor.GOLD, o);
	}

	public void sverbose(String... o)
	{
		log("" + ChatColor.DARK_PURPLE, o);
	}

	public void soverbose(String... o)
	{
		log("" + ChatColor.DARK_AQUA, o);
	}
}
