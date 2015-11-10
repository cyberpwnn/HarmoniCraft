package com.ulticraft.hc.uapi;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class USound
{
	private String sound;
	private Sound iSound;
	private Float volume;
	private Float pitch;
	
	public USound(String sound, Float volume, Float pitch)
	{
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public USound(String sound)
	{
		this.sound = sound;
		this.volume = 1f;
		this.pitch = 1f;
	}
	
	public USound(Sound iSound, Float volume, Float pitch)
	{
		this.iSound = iSound;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public USound(Sound iSound)
	{
		this.iSound = iSound;
		this.volume = 1f;
		this.pitch = 1f;
	}
	
	public void play(Player p)
	{
		if(iSound != null)
		{
			p.playSound(p.getLocation(), iSound, volume, pitch);
		}
		
		if(sound != null)
		{
			String cmd = "playsound " + sound + " " + p.getName() + " ~ ~ ~ " + volume + " " + pitch;
			
			p.getServer().dispatchCommand(p.getServer().getConsoleSender(), cmd);
		}
	}
	
	public String getSound()
	{
		return sound;
	}
	
	public void setSound(String sound)
	{
		this.sound = sound;
	}
	
	public Sound getiSound()
	{
		return iSound;
	}

	public void setiSound(Sound iSound)
	{
		this.iSound = iSound;
	}

	public Float getVolume()
	{
		return volume;
	}
	
	public void setVolume(Float volume)
	{
		this.volume = volume;
	}
	
	public Float getPitch()
	{
		return pitch;
	}
	
	public void setPitch(Float pitch)
	{
		this.pitch = pitch;
	}
}
