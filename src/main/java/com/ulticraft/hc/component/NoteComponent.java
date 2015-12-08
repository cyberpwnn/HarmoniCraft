package com.ulticraft.hc.component;

import org.bukkit.entity.Player;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.Info;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.Depend;
import net.md_5.bungee.api.ChatColor;

@Depend(DataComponent.class)
public class NoteComponent extends Component
{
	public NoteComponent(HarmoniCraft pl)
	{
		super(pl);
	}
	
	public void enable()
	{
		
	}
	
	public void disable()
	{
		
	}
	
	public int get(Player p)
	{
		return pl.gpd(p).getNotes();
	}
	
	public void set(Player p, int n)
	{
		pl.gpd(p).setNotes(n);
	}
	
	public void give(Player p, int n)
	{
		set(p, get(p) + n);
		p.sendMessage(Info.TAG_NOTES + ChatColor.WHITE + String.format(Info.MSG_NOTES_EARNED, String.valueOf(n)));
	}
	
	public void take(Player p, int n)
	{
		set(p, get(p) - n);
		p.sendMessage(Info.TAG_NOTES + ChatColor.WHITE + String.format(Info.MSG_NOTES_SPENT, String.valueOf(n)));
	}
	
	public boolean has(Player p, int n)
	{
		return get(p) >= n;
	}
}
