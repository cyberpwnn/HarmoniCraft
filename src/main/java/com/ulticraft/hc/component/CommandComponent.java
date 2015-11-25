package com.ulticraft.hc.component;

import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.Info;
import com.ulticraft.hc.command.NoteCommandExecutor;
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
		pl.getCommand(Info.CMD_NOTES).setExecutor(new NoteCommandExecutor(pl));
	}
	
	public void disable()
	{
		
	}
}
