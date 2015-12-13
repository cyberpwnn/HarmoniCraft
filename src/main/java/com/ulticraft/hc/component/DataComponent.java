package com.ulticraft.hc.component;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.composite.PlayerData;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.DataManager;
import com.ulticraft.hc.uapi.Title;
import com.ulticraft.hc.uapi.UMap;
import net.md_5.bungee.api.ChatColor;

public class DataComponent extends Component implements Listener
{
	private File base;
	private UMap<Player, PlayerData> cache;
	private Integer[] task;
	private String i;
	private int c = 10;
	
	public DataComponent(HarmoniCraft pl)
	{
		super(pl);
		this.base = new File(pl.getDataFolder(), "data");
		this.cache = new UMap<Player, PlayerData>();
		this.task = null;
		this.i = null;
	}
	
	public void enable()
	{
		if(!base.exists())
		{
			verify(base);
		}
		
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
	
	public void importLegacy(File file)
	{
		final FileConfiguration fc = new YamlConfiguration();
		
		try
		{
			fc.load(file);
			
			Set<String> keys = fc.getConfigurationSection("Notes.Players").getKeys(true);
			final String name = "NAME UNKNOWN (this will fix when they join)";
			
			final String[] mk = keys.toArray(new String[keys.size()]);
			
			task = new Integer[2];
			
			task[1] = 0;
			task[0] = pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
			{
				@Override
				public void run()
				{
					if(task[1] >= mk.length)
					{
						pl.cancelTask(task[0]);
						
						for(final Player i : pl.onlinePlayers())
						{
							i.getWorld().strikeLightningEffect(i.getLocation());
							i.playSound(i.getLocation(), Sound.AMBIENCE_THUNDER, 1.0f, 0.4f);
							i.playSound(i.getLocation(), Sound.AMBIENCE_THUNDER, 1.0f, 1.6f);
							i.playSound(i.getLocation(), Sound.WITHER_DEATH, 1.0f, 2.0f);
							
							pl.scheduleSyncTask(80, new Runnable()
							{
								@Override
								public void run()
								{
									Title t = new Title();
									
									t.setFadeInTime(20);
									t.setFadeOutTime(80);
									t.setStayTime(50);
									t.setTitle(ChatColor.AQUA + "HarmoniCraft");
									t.setSubtitle(ChatColor.YELLOW + "Developed by cyberpwn");
									
									t.send(i);
									
									i.playSound(i.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
								}
							});
						}
						
						return;
					}
					
					i = mk[task[1]];
					task[1]++;
					
					UUID uuid = UUID.fromString(i);
					Integer notes = fc.getInt("Notes.Players." + uuid.toString());
					
					PlayerData pd = new PlayerData();
					pd.setName(name);
					pd.setNotes(notes);
					pd.setPackages("");
					pd.setUuid(uuid.toString());
					
					DataManager dm = new DataManager(pl, toFileName(uuid));
					dm.writeYAML(pd);
					
					int percent = (int) ((double)100 * (double)((double)task[1] / (double)mk.length));
					Title t = new Title();
					t.setFadeInTime(0);
					t.setFadeOutTime(0);
					t.setStayTime(40);
					t.setTitle(ChatColor.LIGHT_PURPLE + "CYBERPWN " + ChatColor.DARK_GRAY + " IS A " + ChatColor.LIGHT_PURPLE + " GOD");
					t.setSubtitle(ChatColor.DARK_GRAY + "Cyberpwn's Charge: " + ChatColor.LIGHT_PURPLE + percent + "%");
					t.setSubSubTitle(ChatColor.LIGHT_PURPLE + "Setting up Ulticraft Source...");
					
					if(percent > 30)
					{
						t.setSubSubTitle(ChatColor.LIGHT_PURPLE + "Patching Player: " + task[1] + " :: + " + uuid);
					}
					
					if(percent > 80)
					{
						t.setSubSubTitle(ChatColor.LIGHT_PURPLE + "Preparing Concurrent Player Injection...");
					}
					
					
					t.send();
					
					c--;
					
					if(c < 0)
					{
						Random r = new Random();

						c = 100 - percent - r.nextInt(30);
						
						for(Player j : pl.onlinePlayers())
						{
							Vector v = new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble());
							
							if(r.nextBoolean())
							{
								v.multiply(-1);
							}
							
							j.getWorld().strikeLightningEffect(j.getLocation().add(v.multiply(9)));
						}
					}
				}
			});
		}
		
		catch(IOException | InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
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
	
	public File toFileName(UUID uuid)
	{
		return new File(base, uuid.toString() + ".yml");
	}
	
	public boolean exists(Player player)
	{
		return toFileName(player).exists();
	}
	
	void verify(File dir)
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
