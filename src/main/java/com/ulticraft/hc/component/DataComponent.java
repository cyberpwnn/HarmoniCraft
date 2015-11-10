package com.ulticraft.hc.component;

import java.io.File;
import java.io.IOException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.composite.PlayerData;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.DataManager;
import com.ulticraft.hc.uapi.UMap;

public class DataComponent extends Component implements Listener
{
	private File base;
	private UMap<Player, PlayerData> cache;
	
	public DataComponent(HarmoniCraft pl)
	{
		super(pl);
		this.base = new File(pl.getDataFolder(), "data");
		this.cache = new UMap<Player, PlayerData>();
	}
	
	public void enable()
	{
		pl.register(this);
		
		for(Player i : pl.onlinePlayers())
		{
			join(i);
		}
		
		super.enable();
	}
	
	public void disable()
	{
		pl.unRegister(this);
		
		for(Player i : pl.onlinePlayers())
		{
			quit(i);
		}
		
		super.disable();
	}
	
	public void create(Player player)
	{
		PlayerData pd = new PlayerData(player);
		cache.put(player, pd);
		pl.o("Created Player " + player.getUniqueId().toString());
	}
	
	public void load(Player player)
	{
		if(cache.containsKey(player))
		{
			pl.f("Failed to find Player " + player.getUniqueId().toString());
			return;
		}
		
		DataManager dm = new DataManager(pl, toFileName(player));
		PlayerData pd = (PlayerData) dm.readYAML(PlayerData.class);
		
		if(pd != null)
		{
			cache.put(player, pd);
			pl.o("Loaded Player " + player.getUniqueId().toString());
		}
		
		else
		{
			pl.f("Failed to load Player " + player.getUniqueId().toString());
		}
	}
	
	public boolean save(Player player)
	{
		if(flush(player))
		{
			cache.remove(player);
			pl.o("Saved Player " + player.getUniqueId().toString());
			return true;
		}
		
		pl.f("Failed to save Player " + player.getUniqueId().toString());
		return false;
	}
	
	public boolean flush(Player player)
	{
		if(!cache.containsKey(player))
		{
			pl.f("Failed to find (flush) Player " + player.getUniqueId().toString());
			return false;
		}
		
		DataManager dm = new DataManager(pl, toFileName(player));
		PlayerData pd = get(player);
		
		if(pd != null)
		{
			dm.writeYAML(pd);
		}
		
		else
		{
			pl.f("Failed to flush player " + player.getUniqueId().toString());
		}
		
		pl.o("Flushed Player " + player.getUniqueId().toString());
		return true;
	}
	
	public PlayerData get(Player player)
	{
		if(!cache.containsKey(player))
		{
			pl.f("Failed to find Player " + player.getUniqueId().toString());
			load(player);
		}
		
		return cache.get(player);
	}
	
	public void join(Player player)
	{
		if(!exists(player))
		{
			create(player);
		}
		
		else
		{
			load(player);
		}
	}
	
	public void quit(Player player)
	{
		if(!save(player))
		{
			pl.f("FAILED TO SAVE PLAYER! " + player.getUniqueId().toString());
		}
	}
	
	public File toFileName(Player player)
	{
		return new File(base, player.getUniqueId().toString() + ".yml");
	}
	
	public boolean exists(Player player)
	{
		return toFileName(player).exists();
	}
	
	private void verify(File dir)
	{
		if(!dir.getParentFile().exists())
		{
			
			verify(dir.getParentFile());
		}
		
		pl.o("Creating Directory " + dir.getPath());
		dir.mkdir();
	}
	
	public void verifyFile(File file)
	{
		if(!file.getParentFile().exists())
		{
			verify(file.getParentFile());
		}
		
		try
		{
			pl.o("Creating File " + file.getPath());
			file.createNewFile();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayer(PlayerJoinEvent e)
	{
		join(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayer(PlayerQuitEvent e)
	{
		quit(e.getPlayer());
	}
}
