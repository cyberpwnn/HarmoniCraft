package com.ulticraft.hc.component;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.Info;
import com.ulticraft.hc.composite.PackageData;
import com.ulticraft.hc.composite.PlayerData;
import com.ulticraft.hc.uapi.Component;
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
		PackageData pDemo = new PackageData();
		pDemo.setCost(20);
		pDemo.setMaterial(Material.ACACIA_DOOR.toString());
		pDemo.setDescription("Description of Package");
		pDemo.setName("Name of package");
		pDemo.setInitial(new UList<String>().qadd("/give $$ diamond 1").qadd("/another command"));
		pDemo.setGrant(new UList<String>().qadd("granted.permission").qadd("another.new.permission"));
		pDemo.setDependencies(new UList<String>().qadd("Package Name").qadd("Another Package"));
		
		try
		{
			pDemo.toYaml().save(demof);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
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
				pl.s("Reading Package Data for " + i.getName());
				FileConfiguration fc = new YamlConfiguration();
				
				try
				{
					fc.load(i);
					PackageData pd = new PackageData(fc);
					pl.s("  Loaded Package: " + pd.toString());
					packages.put(pd.getName(), pd);
					l++;
				}
				
				catch(IOException | InvalidConfigurationException e)
				{
					e.printStackTrace();
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
		if(pd.getReoccurring() != null && pd.getReoccurring() == true)
		{
			return false;
		}
		
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
		
		for(String k : pd.getDependencies())
		{
			if(pl.getPackageComponent().get(k) != null)
			{
				if(!pl.getPackageComponent().has(p, k))
				{
					return;
				}
			}
		}
		
		if(!(pd.getReoccurring() != null && pd.getReoccurring() == true))
		{
			PlayerData pld = pl.gpd(p);
			pld.setPackages(new UList<String>(pl.gpd(p).getPackages()).qadd(getCodeName(pd)).toString(","));
			pl.spd(p, pld);
		}
		
		pl.getNoteComponent().take(p, pd.getCost());
		p.sendMessage(Info.TAG_NOTES + "Unlocked " + pd.getName());
		
		for(String i : pd.getInitial())
		{
			String m = StringUtils.replace(i, "$$", p.getName());
			
			if(m.substring(0, 1).equals("/"))
			{
				m = m.substring(1);
			}
			
			pl.s("Dispatching Command [" + p.getName() + "]: " + ChatColor.GOLD + i + " >> " + ChatColor.AQUA + m);
			Bukkit.dispatchCommand(pl.getServer().getConsoleSender(), m);
		}
		
		for(String i : pd.getGrant())
		{
			pl.s("Granting Permission [" + p.getName() + "]: " + ChatColor.GOLD + i);
			
		}
	}
}
