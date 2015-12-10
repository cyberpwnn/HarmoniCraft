package com.ulticraft.hc.component;

import java.io.File;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.composite.PackageData;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.DataManager;
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
			pl.getDataComponent().verify(base);
		}
	}
	
	public void disable()
	{
		
	}
	
	public void load()
	{
		int l = 0;
		
		for(File i : base.listFiles())
		{
			if(i.isFile() && i.getName().toLowerCase().endsWith(".yml"))
			{
				DataManager dm = new DataManager(pl, i);
				
				try
				{
					PackageData pd = (PackageData) dm.readYAML(PackageData.class);
					
					packages.add(pd);
					
					pl.o("Loded Package: " + pd.getName() + " [" + pd.getCost() + " Notes]");
					l++;
				}
				
				catch(Exception e)
				{
					pl.f("FAILED TO LOAD DATA FOR PACKAGE FILE: " + i.getName() + "At: " + i.getPath());
				}
			}
		}
		
		pl.s("Loaded " + l + " Packages");
	}
}
