package com.ulticraft.hc.component;

import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.Depend;

@Depend(PackageComponent.class)
public class UIComponent extends Component
{
	public UIComponent(HarmoniCraft pl)
	{
		super(pl);
	}
	
	public void enable()
	{

	}
	
	public void disable()
	{
		
	}
}
