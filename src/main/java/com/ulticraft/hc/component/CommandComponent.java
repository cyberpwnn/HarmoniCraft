package com.ulticraft.hc.component;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.Info;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.Depend;

@Depend(DataComponent.class)
public class CommandComponent extends Component
{
	public CommandComponent(HarmoniCraft pl)
	{
		super(pl);
	}
	
	public void enable()
	{
		
	}
	
	public void disable()
	{
		
	}
	
	public boolean process(CommandSender sender, String cmd, String[] args)
	{
		if(cmd.equals(Info.CMD_NOTES))
		{
			if(args.length == 0)
			{
				if(sender instanceof Player)
				{
					sender.sendMessage(String.format(pl.getMessageComponent().getMessageData().getNotesGet(), String.valueOf(pl.gpd((Player) sender).getNotes())));
				}
				
				else
				{
					sender.sendMessage("Player only command! Sorry!");
				}
			}
			
			return true;
		}
		
		return false;
	}
}
