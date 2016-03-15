package com.ulticraft.hc.component;

import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.Info;
import com.ulticraft.hc.composite.PackageData;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.DataManager;
import com.ulticraft.hc.uapi.Depend;
import com.ulticraft.hc.uapi.UList;
import com.ulticraft.hc.uapi.UMap;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;

@Depend(DataComponent.class)
public class PackageComponent extends Component
{
	private File base;
	private UMap<String, PackageData> packages;
	public static Permission perms = null;
	
	public PackageComponent(HarmoniCraft pl)
	{
		super(pl);
	}
	
	public void enable()
	{
		this.base = new File(pl.getDataFolder(), "packages");
		this.packages = new UMap<String, PackageData>();
		
		if(!setupPermissions())
		{
			pl.f("FAILED TO INSTANTIATE VAULT! ENRUE YOU HAVE INSTALLED VAULT");
		}
		
		else
		{
			pl.s("Hooked into Vault");
		}
		
		if(!base.exists())
		{
			pl.getDataComponent().verify(base);
		}
		
		File demo = new File(base, "demo");
		
		if(!demo.exists())
		{
			demo.mkdirs();
		}
		
		File demof = new File(demo, "demo-package.yml");
		PackageData pDemo = new PackageData("Package Name", "Package Description", 20, "perm.something,perm.granted,permission.some.perm", "/give $$ diamond 1,/give $$ emerald 1,", Material.SAPLING.toString());
		DataManager dm = new DataManager(pl, demof);
		dm.writeYAML(pDemo);
	}
	
	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> rsp = pl.getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
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
		p.sendMessage(Info.TAG_NOTES + "Unlocked " + pd.getName());
		
		for(String i : pd.getInitial().split(","))
		{
			String m = StringUtils.replace(i, "$$", p.getName());
			
			if(m.substring(0, 1).equals("/"))
			{
				m = m.substring(1);
			}
			
			pl.s("Dispatching Command [" + p.getName() + "]: " + ChatColor.GOLD + i + " >> " + ChatColor.AQUA + m);
			Bukkit.dispatchCommand(pl.getServer().getConsoleSender(), m);
		}
		
		for(String i : pd.getGrant().split(","))
		{
			pl.s("Granting Permission [" + p.getName() + "]: " + ChatColor.GOLD + i);
			
		}
	}
}
