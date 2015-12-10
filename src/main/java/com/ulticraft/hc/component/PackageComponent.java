package com.ulticraft.hc.component;

import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.composite.PackageData;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.DataManager;
import com.ulticraft.hc.uapi.Depend;
import com.ulticraft.hc.uapi.UList;
import com.ulticraft.hc.uapi.UMap;

@Depend(DataComponent.class)
public class PackageComponent extends Component
{
	private File base;
	private UMap<String, PackageData> packages;
	
	public PackageComponent(HarmoniCraft pl)
	{
		super(pl);
	}
	
	public void enable()
	{
		this.base = new File(pl.getDataFolder(), "packages");
		this.packages = new UMap<String, PackageData>();
		
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
					
					packages.put(StringUtils.remove(i.getName(), ".yml").toLowerCase(), pd);
					
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
	
	public PackageData get(String name)
	{
		for(String i : packages.keySet())
		{
			if(i.equalsIgnoreCase(name))
			{
				return packages.get(i);
			}
		}
		
		for(PackageData i : packages.values())
		{
			if(i.getName().equals(name))
			{
				return i;
			}
			
			if(i.getName().equals(name))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public UList<PackageData> get()
	{
		return new UList<PackageData>(packages.values());
	}
	
	public UList<PackageData> get(Player p)
	{
		UList<String> pd = new UList<String>(pl.gpd(p).getPackages());
		UList<PackageData> pck = new UList<PackageData>();
		
		for(String i : pd)
		{
			PackageData pk = get(i);
			
			if(pk != null)
			{
				pck.add(pk);
			}
		}
		
		return pck;
	}
	
	public String getCodeName(PackageData pd)
	{
		for(String i : packages.keySet())
		{
			if(packages.get(i).equals(pd))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public boolean has(Player p, PackageData pd)
	{
		return get(p).contains(pd);
	}
	
	public boolean has(Player p, String pd)
	{
		return get(p).contains(get(pd));
	}
	
	public void aquire(Player p, PackageData pd)
	{
		if(has(p, pd) || !pl.getNoteComponent().has(p, pd.getCost()))
		{
			return;
		}
		
		pl.gpd(p).setPackages(new UList<String>(pl.gpd(p).getPackages()).qadd(getCodeName(pd)).toString(","));
		pl.getNoteComponent().take(p, pd.getCost());
	}
}
