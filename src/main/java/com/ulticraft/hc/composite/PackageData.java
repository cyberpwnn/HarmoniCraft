package com.ulticraft.hc.composite;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.ulticraft.hc.uapi.UList;

public class PackageData
{
	private String name;
	private Integer cost;
	private String material;
	private UList<String> grant;
	private UList<String> initial;
	private UList<String> dependencies;
	private String description;
	
	public PackageData()
	{
		this.grant = new UList<String>();
		this.initial = new UList<String>();
		this.dependencies = new UList<String>();
	}
	
	@SuppressWarnings("deprecation")
	public PackageData(FileConfiguration fc)
	{
		this.name = fc.getString("package.name");
		this.cost = fc.getInt("package.cost");
		this.grant = new UList<String>(fc.getStringList("package.permissions"));
		this.initial = new UList<String>(fc.getStringList("package.commands"));
		this.dependencies = new UList<String>(fc.getStringList("package.requires"));
		this.material = fc.getString("package.material");
		this.description = fc.getString("package.description");
		
		try
		{
			Material.valueOf(material);
		}
		
		catch(Exception e)
		{
			try
			{
				Integer i = Integer.parseInt(material);
				material = Material.getMaterial(i).toString();
				System.out.println("Selected Material from ID: " + material);
			}
			
			catch(Exception e2)
			{
				e.printStackTrace();
			}
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getCost()
	{
		return cost;
	}

	public void setCost(Integer cost)
	{
		this.cost = cost;
	}

	public String getMaterial()
	{
		return material;
	}

	public void setMaterial(String material)
	{
		this.material = material;
	}

	public UList<String> getGrant()
	{
		return grant;
	}

	public void setGrant(UList<String> grant)
	{
		this.grant = grant;
	}

	public UList<String> getInitial()
	{
		return initial;
	}

	public void setInitial(UList<String> initial)
	{
		this.initial = initial;
	}

	public UList<String> getDependencies()
	{
		return dependencies;
	}

	public void setDependencies(UList<String> dependencies)
	{
		this.dependencies = dependencies;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public FileConfiguration toYaml()
	{
		FileConfiguration fc = new YamlConfiguration();
		
		fc.set("package.name", name);
		fc.set("package.description", description);
		fc.set("package.material", material);
		fc.set("package.cost", cost);
		fc.set("package.commands", initial);
		fc.set("package.permissions", grant);
		fc.set("package.requires", dependencies);
		
		return fc;
	}
	
	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		
		if(o instanceof PackageData)
		{
			PackageData pdx = (PackageData) o;
			
			if(pdx.getCost() == getCost())
			{
				if(pdx.getName().equals(getName()))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public String toString()
	{
		return name + " > " + cost + "n";
	}
}
