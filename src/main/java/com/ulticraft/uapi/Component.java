package com.ulticraft.uapi;

import com.ulticraft.hc.HarmoniCraft;

public class Component
{
	protected final HarmoniCraft pl;
	private boolean enabled;
	
	public Component(HarmoniCraft pl)
	{
		this.pl = pl;
		enabled = false;
	}
	
	public void enable()
	{
		enabled = true;
	}
	
	public void disable()
	{
		enabled = false;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}
