package com.ulticraft.hc.component;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.Info;
import com.ulticraft.hc.composite.PlayerData;
import com.ulticraft.hc.uapi.Area;
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
	private int tsk;
	private int tks;
	private boolean importing;
	private String pc;
	private int pcx;
	private int pcc;
	
	public DataComponent(HarmoniCraft pl)
	{
		super(pl);
		this.base = new File(pl.getDataFolder(), "data");
		this.cache = new UMap<Player, PlayerData>();
		this.task = null;
		this.i = null;
		this.importing = false;
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
			
			pcx = 0;
			pcc = 0;
			importing = true;
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
					if(Math.random() < 0.01)
					{
						for(Player i : pl.onlinePlayers())
						{
							Area a = new Area(i.getLocation().add(0, 20, 0), 48.0);
							fireworks(a.random());
						}
					}
					
					for(int ixx = 0; ixx < 4; ixx++)
					{
						if(task[1] >= mk.length)
						{
							pl.cancelTask(task[0]);
							importing = false;
							
							tks = 0;
							
							tsk = pl.scheduleSyncRepeatingTask(1, 1, new Runnable()
							{
								@Override
								public void run()
								{
									tks++;
									
									if(Math.random() < 0.5)
									{
										for(Player i : pl.onlinePlayers())
										{
											Area a = new Area(i.getLocation().add(0, 20, 0), 48.0);
											fireworks(a.random());
										}
									}
									
									if(tks > 120)
									{
										for(Player i : pl.onlinePlayers())
										{
											Area a = new Area(i.getLocation().add(0, 20, 0), 30.0);
											
											for(int ix = 0; ix < 1; ix++)
											{
												fireworks(a.random());
											}
										}
									}
									
									if(tks > 200)
									{
										pl.cancelTask(tsk);
									}
								}
							});
							
							for(final Player i : pl.onlinePlayers())
							{
								pl.scheduleSyncTask(80, new Runnable()
								{
									@Override
									public void run()
									{
										Title t = new Title();
										
										t.setFadeInTime(5);
										t.setFadeOutTime(80);
										t.setStayTime(80);
										t.setTitle(ChatColor.AQUA + "HarmoniCraft Notes");
										t.setSubtitle(ChatColor.YELLOW + "Developed by cyberpwn");
										i.getWorld().playSound(i.getLocation(), Sound.AMBIENCE_CAVE, 1f, 1.8f);
										
										t.send(i);
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
						
						for(Player c : pl.onlinePlayers())
						{
							if(c.getUniqueId().toString().equals(pd.getUuid()))
							{
								pd.setName(c.getName());
								pl.spd(c, pd);
								c.sendMessage(Info.TAG_HARMONICRAFT + "----------------------------------------");
								c.sendMessage(Info.TAG_HARMONICRAFT + "       Your Playerdata has been imported. ");
								c.sendMessage(Info.TAG_HARMONICRAFT + "      If your notes do not show up, tell me!");
								c.sendMessage(Info.TAG_HARMONICRAFT + "                                       - cyberpwn");
								c.sendMessage(Info.TAG_HARMONICRAFT + "----------------------------------------");
							}
						}
						
						DataManager dm = new DataManager(pl, toFileName(uuid));
						dm.writeYAML(pd);
						
						int percent = (int) ((double) 100 * (double) ((double) task[1] / (double) mk.length));
						Title t = new Title();
						t.setFadeInTime(0);
						t.setFadeOutTime(0);
						t.setStayTime(40);
						t.setSubtitle(ChatColor.DARK_GRAY + "Progress: " + ChatColor.AQUA + percent + "%");
						t.setSubSubTitle(ChatColor.AQUA + "Setting up Source...");
						
						pc = percent + "%";
						pcc = percent;
						
						if(pcc >= 85)
						{
							t.setTitle(ChatColor.GOLD + "Firework Show at HUB: " + (100 - pcc));
						}
						
						if(pcc != pcx)
						{
							pcx = pcc;
							pl.o("Importing PlayerData: " + percent + "%");
							
							if(pcc >= 90)
							{
								for(Player ci : pl.onlinePlayers())
								{
									ci.playSound(ci.getLocation(), Sound.FIREWORK_BLAST, 1f, 0.1f);
								}
							}
						}
						
						if(percent > 5)
						{
							t.setSubSubTitle(ChatColor.AQUA + "Harmonicraft Notes by Cyberpwn");
						}
						
						if(percent > 10)
						{
							t.setSubSubTitle(ChatColor.AQUA + "Your notes are being transfered.");
						}
						
						if(percent > 15)
						{
							t.setSubSubTitle(ChatColor.AQUA + "Check all the commands with /note ?");
						}
						
						if(percent > 20)
						{
							t.setSubSubTitle(ChatColor.AQUA + "Full GUI Support");
						}
						
						if(percent > 25)
						{
							t.setSubSubTitle(ChatColor.AQUA + "Performence Driven");
						}
						
						if(percent > 30)
						{
							t.setSubSubTitle(ChatColor.AQUA + "Open Source (github.com/cyberpwnn)");
						}
						
						if(percent > 61)
						{
							t.setSubSubTitle(ChatColor.AQUA + "Patching Player: " + task[1] + " :: + " + uuid);
						}
						
						if(percent > 97)
						{
							t.setSubSubTitle(ChatColor.AQUA + "Preparing Concurrent Player Injection...");
						}
						
						t.send();
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
			pd.setName(player.getName());
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
	
	public void put(Player p, PlayerData pd)
	{
		cache.put(p, pd);
	}
	
	public void join(Player player)
	{
		if(importing)
		{
			pl.scheduleSyncTask(0, new Runnable()
			{
				@Override
				public void run()
				{
					player.kickPlayer(ChatColor.GOLD + "Harmonicraft is Updating (" + pc + ")");
				}
			});
			
			return;
		}
		
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
		if(importing)
		{
			return;
		}
		
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
	
	public void fireworks(Location location)
	{
		Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		
		Random r = new Random();
		
		int rt = r.nextInt(4) + 1;
		Type type = Type.BALL;
		if(rt == 1)
			type = Type.BALL;
		if(rt == 2)
			type = Type.BALL_LARGE;
		if(rt == 3)
			type = Type.BURST;
		if(rt == 4)
			type = Type.CREEPER;
		if(rt == 5)
			type = Type.STAR;
		
		int r1i = r.nextInt(17) + 1;
		int r2i = r.nextInt(17) + 1;
		Color c1 = getColor(r1i);
		Color c2 = getColor(r2i);
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
		fwm.addEffect(effect);
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
	}
	
	private Color getColor(int i)
	{
		Color c = null;
		if(i == 1)
		{
			c = Color.AQUA;
		}
		
		if(i == 2)
		{
			c = Color.BLACK;
		}
		
		if(i == 3)
		{
			c = Color.BLUE;
		}
		
		if(i == 4)
		{
			c = Color.FUCHSIA;
		}
		
		if(i == 5)
		{
			c = Color.GRAY;
		}
		
		if(i == 6)
		{
			c = Color.GREEN;
		}
		
		if(i == 7)
		{
			c = Color.LIME;
		}
		
		if(i == 8)
		{
			c = Color.MAROON;
		}
		
		if(i == 9)
		{
			c = Color.NAVY;
		}
		
		if(i == 10)
		{
			c = Color.OLIVE;
		}
		
		if(i == 11)
		{
			c = Color.ORANGE;
		}
		
		if(i == 12)
		{
			c = Color.PURPLE;
		}
		
		if(i == 13)
		{
			c = Color.RED;
		}
		
		if(i == 14)
		{
			c = Color.SILVER;
		}
		
		if(i == 15)
		{
			c = Color.TEAL;
		}
		
		if(i == 16)
		{
			c = Color.WHITE;
		}
		
		if(i == 17)
		{
			c = Color.YELLOW;
		}
		
		return c;
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
