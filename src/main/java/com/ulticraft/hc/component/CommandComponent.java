package com.ulticraft.hc.component;

import org.bukkit.command.CommandSender;
import com.ulticraft.hc.HarmoniCraft;
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
		return false;
	}
}
