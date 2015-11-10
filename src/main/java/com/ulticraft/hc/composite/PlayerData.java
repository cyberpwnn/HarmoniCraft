package com.ulticraft.hc.composite;

import org.bukkit.entity.Player;
import com.ulticraft.hc.uapi.Table;

public class PlayerData implements Table
{
	private String uuid;
	private String name;
	private Integer notes;
	
	public PlayerData(Player player)
	{
		this.uuid = player.getUniqueId().toString();
		this.name = player.getName();
		this.notes = 0;
	}
	
	public String getUuid()
	{
		return uuid;
	}
	
	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Integer getNotes()
	{
		return notes;
	}
	
	public void setNotes(Integer notes)
	{
		this.notes = notes;
	}
}
