package com.ulticraft.uapi;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.ulticraft.hc.HarmoniCraft;

public class DataManager
{
	private File file;
	
	public DataManager(HarmoniCraft pl, String localPath)
	{
		file = new File(pl.getDataFolder(), localPath);
	}
	
	public DataManager(HarmoniCraft pl, File file)
	{
		this.file = file;
	}
	
	public void writeYAML(Object o)
	{
		verifyFile(file);
		FileConfiguration fc = new YamlConfiguration();
		Reflector r = new Reflector();
		MetaData md = r.getMetaData(o.getClass());
		
		for(MetaReference i : md.getReferences())
		{
			fc.set(i.getTarget().getPathYAML(), r.get(o, i));
		}
		
		try
		{
			fc.save(file);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Object readYAML(Class<?> clazz)
	{
		FileConfiguration fc = new YamlConfiguration();
		Reflector r = new Reflector();
		MetaData md = r.getMetaData(clazz);
		
		if(!file.exists())
		{
			verifyFile(file);
			writeYAML(r.instantiate(clazz));
		}
		
		try
		{
			fc.load(file);
			
			Object k = r.instantiate(clazz);
			
			for(MetaReference i : md.getReferences())
			{
				Object o = fc.get(i.getTarget().getPathYAML());
				if(o != null)
				{
					r.set(k, i, r.valueOf(i, o));
				}
			}
			
			return k;
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void verify(File dir)
	{
		if(!dir.getParentFile().exists())
		{
			verify(dir.getParentFile());
		}
		
		dir.mkdir();
	}
	
	private void verifyFile(File file)
	{
		if(!file.getParentFile().exists())
		{
			verify(file.getParentFile());
		}
		
		try
		{
			file.createNewFile();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
