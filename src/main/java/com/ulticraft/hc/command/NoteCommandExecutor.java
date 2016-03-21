package com.ulticraft.hc.command;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.Info;
import net.md_5.bungee.api.ChatColor;

public class NoteCommandExecutor implements CommandExecutor
{
	private HarmoniCraft pl;
	
	public NoteCommandExecutor(HarmoniCraft pl)
	{
		this.pl = pl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		boolean isPlayer = sender instanceof Player;
		Player player = isPlayer ? (Player) sender : null;
		
		if(args.length == 0)
		{
			if(isPlayer)
			{
				player.sendMessage(String.format(Info.MSG_NOTES_HAVE, String.valueOf(pl.gpd(player).getNotes())));
			}
			
			else
			{
				pl.msg(sender, Info.CMD_HELP_ADMIN_NOTES);
			}
		}
		
		else
		{
			if(args[0].equalsIgnoreCase("buy"))
			{
				pl.getUiComponent().openBuy(player);
				return true;
			}
			
			else if(args[0].equalsIgnoreCase("pay"))
			{
				if(!isPlayer)
				{
					sender.sendMessage("You dont have a balance.");
					return true;
				}
				
				if(args.length == 3)
				{
					if(pl.canFindPlayer(args[1]))
					{
						try
						{
							int gems = Integer.parseInt(args[2]);
							
							if(gems < 0)
							{
								pl.msg(sender, String.format(Info.MSG_NOT_POSITIVE, String.valueOf(gems)));
							}
							
							else if(gems < 1)
							{
								pl.msg(sender, String.format(Info.MSG_NOT_ZERO, String.valueOf(gems)));
							}
							
							else
							{
								if(!pl.getNoteComponent().has((Player) sender, gems))
								{
									sender.sendMessage(Info.TAG_NOTES + "You dont have that many notes.");
									return true;
								}
								
								pl.getNoteComponent().take((Player) sender, gems);
								pl.getNoteComponent().give(pl.findPlayer(args[1]), gems);
								pl.msg(sender, String.format(Info.MSG_NOTES_PAID, String.valueOf(gems), pl.findPlayer(args[1]).getName()));
								pl.msg(pl.findPlayer(args[1]), String.format(Info.MSG_NOTES_PAID_FROM, ((Player) sender).getName(), String.valueOf(gems)));
							}
						}
						
						catch(NumberFormatException e)
						{
							pl.msg(sender, String.format(Info.MSG_NOT_NUMBER, args[2]));
						}
					}
					
					else
					{
						pl.msg(sender, String.format(Info.MSG_CANT_FIND_PLAYER, args[1]));
					}
				}
				
				else
				{
					pl.msg(sender, Info.TAG_NOTES + "/note pay <player> <ammount>");
				}
			}
			
			else if(args[0].equalsIgnoreCase("owned"))
			{
				pl.getUiComponent().openOwned(player);
				return true;
			}
			
			else if(!sender.hasPermission(Info.PERM_GOD))
			{
				pl.msg(sender, Info.CMD_HELP_NOTES);
				return true;
			}
			
			else if(sender.hasPermission(Info.PERM_GOD))
			{
				if(args[0].equalsIgnoreCase("import"))
				{
					if(isPlayer)
					{
						return true;
					}
					
					pl.getDataComponent().importLegacy(new File(pl.getDataFolder(), "legacy.yml"));
				}
				
				else if(args[0].equalsIgnoreCase("reload"))
				{
					sender.sendMessage(ChatColor.GREEN + "HarmoniCraft (Plugin Reloaded)");
					Bukkit.getServer().getPluginManager().disablePlugin(Bukkit.getServer().getPluginManager().getPlugin("HarmoniCraft"));
					Bukkit.getServer().getPluginManager().enablePlugin(Bukkit.getServer().getPluginManager().getPlugin("HarmoniCraft"));
				}
				
				else if(args[0].equalsIgnoreCase("get"))
				{
					if(args.length == 2)
					{
						if(pl.canFindPlayer(args[1]))
						{
							pl.msg(sender, String.format(Info.MSG_PLAYER_GEM_HAS, pl.findPlayer(args[1]).getName(), pl.gpd(pl.findPlayer(args[1])).getNotes().toString()));
						}
						
						else
						{
							pl.msg(sender, String.format(Info.MSG_CANT_FIND_PLAYER, args[1]));
						}
					}
					
					else
					{
						pl.msg(sender, Info.CMD_HELP_ADMIN_NOTES);
					}
				}
				
				else if(args[0].equalsIgnoreCase("give"))
				{
					if(args.length == 3)
					{
						if(pl.canFindPlayer(args[1]))
						{
							try
							{
								int gems = Integer.parseInt(args[2]);
								
								if(gems < 0)
								{
									pl.msg(sender, String.format(Info.MSG_NOT_POSITIVE, String.valueOf(gems)));
								}
								
								else if(gems < 1)
								{
									pl.msg(sender, String.format(Info.MSG_NOT_ZERO, String.valueOf(gems)));
								}
								
								else
								{
									pl.getNoteComponent().give(pl.findPlayer(args[1]), gems);
									pl.msg(sender, String.format(Info.MSG_NOTES_GAVE, String.valueOf(gems), pl.findPlayer(args[1]).getName()));
								}
							}
							
							catch(NumberFormatException e)
							{
								pl.msg(sender, String.format(Info.MSG_NOT_NUMBER, args[2]));
							}
						}
						
						else
						{
							pl.msg(sender, String.format(Info.MSG_CANT_FIND_PLAYER, args[1]));
						}
					}
					
					else
					{
						pl.msg(sender, Info.CMD_HELP_ADMIN_NOTES);
					}
				}
				
				else if(args[0].equalsIgnoreCase("take"))
				{
					if(args.length == 3)
					{
						if(pl.canFindPlayer(args[1]))
						{
							try
							{
								int gems = Integer.parseInt(args[2]);
								
								if(gems < 0)
								{
									pl.msg(sender, String.format(Info.MSG_NOT_POSITIVE, String.valueOf(gems)));
								}
								
								else if(gems < 1)
								{
									pl.msg(sender, String.format(Info.MSG_NOT_ZERO, String.valueOf(gems)));
								}
								
								else
								{
									if(pl.getNoteComponent().has(pl.findPlayer(args[1]), gems))
									{
										pl.getNoteComponent().take(pl.findPlayer(args[1]), gems);
										pl.msg(sender, String.format(Info.MSG_NOTES_TOOK, String.valueOf(gems), pl.findPlayer(args[1]).getName()));
									}
									
									else
									{
										pl.msg(sender, String.format(Info.MSG_NOTES_NOT_ENOUGH, pl.findPlayer(args[1]).getName(), String.valueOf(pl.getNoteComponent().get(pl.findPlayer(args[1])))));
									}
								}
							}
							
							catch(NumberFormatException e)
							{
								pl.msg(sender, String.format(Info.MSG_NOT_NUMBER, args[2]));
							}
						}
						
						else
						{
							pl.msg(sender, String.format(Info.MSG_CANT_FIND_PLAYER, args[1]));
						}
					}
					
					else
					{
						pl.msg(sender, Info.CMD_HELP_ADMIN_NOTES);
					}
				}
				
				else
				{
					pl.msg(sender, Info.CMD_HELP_ADMIN_NOTES);
				}
			}
			
			else
			{
				pl.msg(sender, Info.CMD_HELP_NOTES);
			}
		}
		
		return true;
	}
}
