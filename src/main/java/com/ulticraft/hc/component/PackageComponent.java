package com.ulticraft.hc.component;

import java.io.File;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.composite.PackageData;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.Depend;
import com.ulticraft.hc.uapi.UList;

@Depend(DataComponent.class)
public class PackageComponent extends Component
{
	private File base;
	private UList<PackageData> packages;
	
	public PackageComponent(HarmoniCraft pl)
	{
		super(pl);
	}
	
	public void enable()
	{
		this.base = new File(pl.getDataFolder(), "packages");
		this.packages = new UList<PackageData>();
		
		if(!base.exists())
		{
			base.mkdir();
		}
		
		for(File i : base.listFiles())
		{
			if(i.isFile() && i.getName().toLowerCase().endsWith(".yml"))
			{
				
			}
		}
	}
	
	public void disable()
	{
		
	}
}
