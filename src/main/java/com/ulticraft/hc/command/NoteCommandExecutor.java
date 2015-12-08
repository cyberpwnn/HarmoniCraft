package com.ulticraft.hc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.Info;

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
			if(sender.hasPermission(Info.PERM_GOD))
			{
				if(args[0].equalsIgnoreCase("get"))
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
