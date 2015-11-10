package com.ulticraft.uapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import com.ulticraft.uapi.Gui.Pane.Element;
import com.ulticraft.uapi.Gui.Pane.Element.Trigger;

/**
 * The Gui Object is built within itself. All components are used internally,
 * meaning this can be copied and pasted into any project, or kept within. All i
 * ask is that you <STRONG>KEEP THIS HERE</STRONG>
 * <p>
 * <p>
 * 
 * To create a new gui, simply instatiate it with a player, and your plugin
 * instance Then simply add gui panes, and elements within. Please view the
 * tutorials at the project home page:
 * <a href="http://ulticraft.com/develop/">Ulticraft.com/develop</a>
 * 
 * @author cyberpwn
 * 		
 */
public class Gui implements Listener
{
	public enum TriggerType
	{
		LEFT_CLICK, RIGHT_CLICK
	}
	
	public enum LinkType
	{
		HOME, NORMAL
	}
	
	public enum PaneState
	{
		OPEN, CLOSED
	}
	
	private ArrayList<Pane> panes;
	private Player player;
	private UUID openPane;
	private UUID defaultPane;
	private UUID uuid;
	private SoundEffect defaultPaneEffect;
	private boolean verbose;
	private boolean closing;
	private ConsoleCommandSender c;
	
	public Gui(Player player, Plugin pl)
	{
		this.player = player;
		
		c = pl.getServer().getConsoleSender();
		uuid = UUID.randomUUID();
		panes = new ArrayList<Gui.Pane>();
		closing = false;
		
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}
	
	public boolean isVerbose()
	{
		return verbose;
	}
	
	public void setVerbose(boolean verbose)
	{
		this.verbose = verbose;
	}
	
	public void v(String s)
	{
		if(isVerbose())
		{
			c.sendMessage(ChatColor.AQUA + "GUI: " + s);
		}
	}
	
	public void close()
	{
		player.closeInventory();
	}
	
	public ArrayList<Pane> getPanes()
	{
		return panes;
	}
	
	public void setPanes(ArrayList<Pane> panes)
	{
		this.panes = panes;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public UUID getUuid()
	{
		return uuid;
	}
	
	public UUID getDefaultPane()
	{
		return defaultPane;
	}
	
	public void setDefaultPane(UUID defaultPane)
	{
		this.defaultPane = defaultPane;
	}
	
	public SoundEffect getDefaultPaneEffect()
	{
		return defaultPaneEffect;
	}
	
	public void setDefaultPaneEffect(SoundEffect defaultPaneEffect)
	{
		this.defaultPaneEffect = defaultPaneEffect;
	}
	
	public UUID getOpenPane()
	{
		return openPane;
	}
	
	public void show()
	{
		if(defaultPane == null)
		{
			return;
		}
		
		else
		{
			if(resolvePane(defaultPane) != null)
			{
				resolvePane(defaultPane).show();
			}
		}
	}
	
	public Pane resolvePane(UUID uuid)
	{
		for(Pane i : panes)
		{
			if(i.getUuid().equals(uuid))
			{
				return i;
			}
		}
		
		return resolvePane(defaultPane);
	}
	
	public String toString()
	{
		String s = "\nGUI: " + getUuid();
		
		for(Pane i : getPanes())
		{
			s = s + "\n  " + i.toString();
		}
		
		return s;
	}
	
	public boolean isClosing()
	{
		return closing;
	}
	
	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent e)
	{
		if(!e.getPlayer().equals(player))
		{
			return;
		}
		
		if(openPane != null)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e)
	{
		if(!e.getPlayer().equals(player))
		{
			return;
		}
		
		if(openPane != null)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e)
	{
		if(!e.getWhoClicked().equals(player))
		{
			return;
		}
		
		for(Pane i : panes)
		{
			if(i.getTitle().equals(e.getInventory().getTitle()))
			{
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent e)
	{
		if(!e.getPlayer().equals(player))
		{
			return;
		}
		
		for(Pane i : panes)
		{
			if(i.getTitle().equals(e.getInventory().getTitle()))
			{
				i.setState(PaneState.CLOSED);
				openPane = null;
				
				v(e.getPlayer().getName() + " Closed Pane: " + i.getTitle() + " :: " + i.getUuid().toString());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onGuiClick(InventoryClickEvent e)
	{
		if(!e.getWhoClicked().equals(player))
		{
			return;
		}
		
		if(e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta())
		{
			return;
		}
		
		for(Pane i : panes)
		{
			if(i.getTitle().equals(e.getInventory().getTitle()))
			{
				for(Element j : i.getElements())
				{
					if(j.getTitle().equals(e.getCurrentItem().getItemMeta().getDisplayName()))
					{
						v(e.getWhoClicked().getName() + " Clicked Pane: @" + i.getTitle() + "/" + j.getTitle() + " :: " + i.getUuid().toString() + "/" + j.getUuid().toString());
						
						if(j.getLink() != null)
						{
							if(e.getAction().equals(InventoryAction.PICKUP_ALL))
							{
								j.getLink().transfer();
							}
						}
						
						if(j.getRLink() != null)
						{
							if(e.getAction().equals(InventoryAction.PICKUP_HALF))
							{
								j.getRLink().transfer();
							}
						}
						
						if(j.getQuickRunnable() != null)
						{
							if(e.getAction().equals(InventoryAction.PICKUP_ALL))
							{
								j.getQuickRunnable().run();
							}
						}
						
						for(Trigger k : j.getTriggers())
						{
							if(e.getAction().equals(InventoryAction.PICKUP_ALL))
							{
								if(k.getType().equals(TriggerType.LEFT_CLICK))
								{
									k.run();
								}
							}
							
							else if(e.getAction().equals(InventoryAction.PICKUP_HALF))
							{
								if(k.getType().equals(TriggerType.RIGHT_CLICK))
								{
									k.run();
								}
							}
						}
					}
				}
				
				e.setCancelled(true);
			}
		}
	}
	
	public class SoundEffect
	{
		private Sound sound;
		private Float pitch;
		private Float volume;
		
		public SoundEffect(Sound sound, Float pitch, Float volume)
		{
			this.sound = sound;
			this.pitch = pitch;
			this.volume = volume;
		}
		
		public SoundEffect(Sound sound, Float pitch)
		{
			this.sound = sound;
			this.pitch = pitch;
		}
		
		public SoundEffect(Sound sound)
		{
			this.sound = sound;
		}
		
		public Sound getSound()
		{
			return sound;
		}
		
		public void setSound(Sound sound)
		{
			this.sound = sound;
		}
		
		public Float getPitch()
		{
			if(pitch == null)
			{
				return 1f;
			}
			
			return pitch;
		}
		
		public void setPitch(Float pitch)
		{
			this.pitch = pitch;
		}
		
		public Float getVolume()
		{
			if(volume == null)
			{
				return 0.4f;
			}
			
			return volume;
		}
		
		public void setVolume(Float volume)
		{
			this.volume = volume;
		}
		
		public void play()
		{
			if(sound == null)
			{
				return;
			}
			
			else
			{
				player.playSound(player.getLocation(), getSound(), getVolume(), getPitch());
			}
		}
	}
	
	public class Pane
	{
		private ArrayList<Element> elements;
		private String title;
		private Inventory inventory;
		private int size;
		private PaneState state;
		private UUID uuid;
		
		public Pane(String title)
		{
			this.title = title;
			
			state = PaneState.CLOSED;
			elements = new ArrayList<Gui.Pane.Element>();
			uuid = UUID.randomUUID();
			
			if(defaultPane == null)
			{
				setDefault();
			}
			
			panes.add(this);
			
			v("Create Pane: " + getTitle() + " :: " + getUuid().toString());
		}
		
		public void show()
		{
			if(getOpenPane() == null)
			{
			
			}
			
			else
			{
				resolvePane(getOpenPane()).setState(PaneState.CLOSED);
			}
			
			if(defaultPaneEffect != null)
			{
				defaultPaneEffect.play();
			}
			
			close();
			setState(PaneState.OPEN);
			openPane = getUuid();
			build();
		}
		
		public void update()
		{
			if(getOpenPane() == getUuid())
			{
				build();
			}
			
			else
			{
				show();
			}
		}
		
		public void build()
		{
			build(true);
		}
		
		public void build(boolean live)
		{
			v("Pane: Build " + getTitle() + " :: " + getUuid().toString());
			
			int size = 0;
			
			for(Element i : elements)
			{
				if(i.getCoordinate().getSlot() > size)
				{
					size = i.getCoordinate().getSlot();
				}
			}
			
			size++;
			
			if(size % 9 != 0)
			{
				size = size + (9 - (size % 9));
			}
			
			inventory = player.getServer().createInventory(null, size, title);
			
			for(Element i : elements)
			{
				ItemStack itemStack = new ItemStack(i.getMaterial());
				ItemMeta itemMeta = itemStack.getItemMeta();
				
				itemMeta.setDisplayName(i.getTitle());
				
				if(!i.getDescription().isEmpty())
				{
					itemMeta.setLore(i.getDescription());
				}
				
				if(i.isGlistening())
				{
					itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
				}
				
				itemStack.setItemMeta(itemMeta);
				itemStack.setAmount(i.getSize());
				
				inventory.setItem(i.getCoordinate().getSlot(), itemStack);
				
				v("  Pack: " + i.getTitle() + " >> " + getTitle() + " :: " + i.getUuid().toString() + "/" + getUuid().toString());
			}
			
			v("Pane: Build Complete S:" + size + " E:" + getElements().size());
			
			if(getState().equals(PaneState.OPEN) && live)
			{
				player.openInventory(inventory);
			}
		}
		
		public void breakElements()
		{
			elements = new ArrayList<Gui.Pane.Element>();
		}
		
		public Element getElement(int s)
		{
			for(Element i : elements)
			{
				if(i.getCoordinate().getSlot() == s)
				{
					return i;
				}
			}
			
			return null;
		}
		
		public void setDefault()
		{
			defaultPane = getUuid();
		}
		
		public PaneState getState()
		{
			return state;
		}
		
		public void setState(PaneState state)
		{
			this.state = state;
		}
		
		public ArrayList<Element> getElements()
		{
			return elements;
		}
		
		public void setElements(ArrayList<Element> elements)
		{
			this.elements = elements;
			build();
		}
		
		public String getTitle()
		{
			return title;
		}
		
		public void setTitle(String title)
		{
			this.title = title;
			build();
		}
		
		public Inventory getInventory()
		{
			return inventory;
		}
		
		public void setInventory(Inventory inventory)
		{
			this.inventory = inventory;
		}
		
		public int getSize()
		{
			return size;
		}
		
		public void setSize(int size)
		{
			this.size = size;
			build();
		}
		
		public UUID getUuid()
		{
			return uuid;
		}
		
		public String toString()
		{
			String s = "PANE: " + getUuid().toString() + "TITLE: " + getTitle() + " SIZE: " + getSize();
			
			for(Element i : getElements())
			{
				s = s + "\n    " + i.toString();
			}
			
			return s;
		}
		
		public class Element
		{
			private ArrayList<Trigger> triggers;
			private String title;
			private ArrayList<String> description;
			private Material material;
			private Coordinate coordinate;
			private int size;
			private boolean glistening;
			private UUID uuid;
			private Link link;
			private Link rlink;
			private Runnable quickRunnable;
			
			public Element(String title, Material material, int x, int y)
			{
				this.title = title;
				this.material = material;
				
				size = 1;
				triggers = new ArrayList<Gui.Pane.Element.Trigger>();
				description = new ArrayList<String>();
				coordinate = new Coordinate(x, y);
				uuid = UUID.randomUUID();
				
				elements.add(this);
				
				v("Create Element: " + getTitle() + "/" + Pane.this.getTitle() + " :: " + getUuid().toString() + "/" + Pane.this.getUuid().toString());
			}
			
			public Element(String title, Material material, int slot)
			{
				this.title = title;
				this.material = material;
				
				size = 1;
				triggers = new ArrayList<Gui.Pane.Element.Trigger>();
				description = new ArrayList<String>();
				coordinate = new Coordinate(slot);
				uuid = UUID.randomUUID();
				
				elements.add(this);
				
				v("Create Element: " + getTitle() + "/" + Pane.this.getTitle() + " :: " + getUuid().toString() + "/" + Pane.this.getUuid().toString());
			}
			
			public Element(String title, Material material, Coordinate coordinate)
			{
				this.title = title;
				this.coordinate = coordinate;
				this.material = material;
				
				size = 1;
				triggers = new ArrayList<Gui.Pane.Element.Trigger>();
				description = new ArrayList<String>();
				uuid = UUID.randomUUID();
				
				elements.add(this);
				
				v("Create Element: " + getTitle() + "/" + Pane.this.getTitle() + " :: " + getUuid().toString() + "/" + Pane.this.getUuid().toString());
			}
			
			public Link getLink()
			{
				return link;
			}
			
			public Link getRLink()
			{
				return rlink;
			}
			
			public void setLink(Link link)
			{
				this.link = link;
			}
			
			public Element setLink(Pane pane)
			{
				link = new Link(pane);
				return this;
			}
			
			public void setRLink(Link link)
			{
				this.rlink = link;
			}
			
			public Element setRLink(Pane pane)
			{
				rlink = new Link(pane);
				return this;
			}
			
			public Runnable getQuickRunnable()
			{
				return quickRunnable;
			}
			
			public Element setQuickRunnable(Runnable quickRunnable)
			{
				this.quickRunnable = quickRunnable;
				return this;
			}
			
			public Element setBullet(String information)
			{
				resetDescription();
				description.add(ChatColor.AQUA + "\u2712 " + information);
				return this;
			}
			
			public Element addBullet(String information)
			{
				description.add(ChatColor.AQUA + "\u2712 " + information);
				return this;
			}
			
			public Element setInfo(String information)
			{
				resetDescription();
				description.add(ChatColor.GOLD + "\u270E " + information);
				return this;
			}
			
			public Element addInfo(String information)
			{
				description.add(ChatColor.GOLD + "\u270E " + information);
				return this;
			}
			
			public Element setRequirement(String information)
			{
				resetDescription();
				description.add(ChatColor.GREEN + "\u2713 " + information);
				return this;
			}
			
			public Element addRequirement(String information)
			{
				description.add(ChatColor.GREEN + "\u2713 " + information);
				return this;
			}
			
			public Element setFailedRequirement(String information)
			{
				resetDescription();
				description.add(ChatColor.RED + "\u2718 " + information);
				return this;
			}
			
			public Element addFailedRequirement(String information)
			{
				description.add(ChatColor.RED + "\u2718 " + information);
				return this;
			}
			
			public Element resetDescription()
			{
				description = new ArrayList<String>();
				return this;
			}
			
			public void rebuild()
			{
				Iterator<Element> i = elements.iterator();
				
				while(i.hasNext())
				{
					Element e = i.next();
					
					if(e.getUuid().equals(getUuid()))
					{
						i.remove();
					}
				}
				
				elements.add(this);
				build();
			}
			
			public int getSize()
			{
				return size;
			}
			
			public void setSize(int size)
			{
				this.size = size;
				rebuild();
			}
			
			public Material getMaterial()
			{
				return material;
			}
			
			public void setMaterial(Material material)
			{
				this.material = material;
				rebuild();
			}
			
			public Coordinate getCoordinate()
			{
				return coordinate;
			}
			
			public void setCoordinate(Coordinate coordinate)
			{
				this.coordinate = coordinate;
				rebuild();
			}
			
			public boolean isGlistening()
			{
				return glistening;
			}
			
			public Element setGlistening(boolean glistening)
			{
				this.glistening = glistening;
				rebuild();
				return this;
			}
			
			public ArrayList<Trigger> getTriggers()
			{
				return triggers;
			}
			
			public void setTriggers(ArrayList<Trigger> triggers)
			{
				this.triggers = triggers;
			}
			
			public String getTitle()
			{
				return title;
			}
			
			public void setTitle(String title)
			{
				this.title = title;
				rebuild();
			}
			
			public ArrayList<String> getDescription()
			{
				return description;
			}
			
			public void setDescription(ArrayList<String> description)
			{
				this.description = description;
				rebuild();
			}
			
			public UUID getUuid()
			{
				return uuid;
			}
			
			public String toString()
			{
				String s = "ELEMENT: " + getUuid().toString() + " MATERIAL: " + getMaterial().toString().toLowerCase() + " TITLE: " + getTitle() + "\n      " + getCoordinate().toString();
				
				for(Trigger i : getTriggers())
				{
					s = s + "\n      " + i.toString();
				}
				
				return s;
			}
			
			public class Coordinate
			{
				private Integer x;
				private Integer y;
				private Integer s;
				
				public Coordinate(Integer x, Integer y)
				{
					if(x > 4)
					{
						x = 4;
					}
					
					if(x < -4)
					{
						x = -4;
					}
					
					if(y > 9)
					{
						y = 9;
					}
					
					if(y < 1)
					{
						y = 1;
					}
					
					this.x = x;
					this.y = y;
					this.s = getPosition(x, y);
				}
				
				public Coordinate(Integer s)
				{
					if(s > (9 * 9) - 1)
					{
						s = (9 * 9) - 1;
					}
					
					this.s = s;
					this.y = (int) ((s / 9) + 1);
					this.x = (s - ((y - 1) * 9)) - 4;
				}
				
				public Integer getSlot()
				{
					return s;
				}
				
				public Integer getX()
				{
					return x;
				}
				
				public Integer getY()
				{
					return y;
				}
				
				private int getPosition(int x, int y)
				{
					return ((y - 1) * 9) + (x + 4);
				}
				
				public void setSlot(Integer s)
				{
					if(s > (9 * 9) - 1)
					{
						s = (9 * 9) - 1;
					}
					
					this.s = s;
					this.y = (int) ((s / 9) + 1);
					this.x = (s - ((y - 1) * 9)) - 4;
				}
				
				public void setX(Integer x)
				{
					if(x > 4)
					{
						x = 4;
					}
					
					if(x < -4)
					{
						x = -4;
					}
					
					this.x = x;
					
					s = getPosition(x, y);
				}
				
				public void setY(Integer y)
				{
					if(y > 9)
					{
						y = 9;
					}
					
					if(y < 1)
					{
						y = 1;
					}
					
					this.y = y;
					
					s = getPosition(x, y);
				}
				
				public String toString()
				{
					return "COORDINATE: " + getX() + "," + getY() + " (" + getSize() + ")";
				}
			}
			
			public class Link
			{
				private UUID link;
				private LinkType type;
				
				public Link(Pane pane)
				{
					link = pane.getUuid();
					type = LinkType.NORMAL;
					
					Element.this.setLink(this);
				}
				
				public Link setType(LinkType type)
				{
					this.type = type;
					return this;
				}
				
				public UUID getLink()
				{
					return link;
				}
				
				public Link setLink(UUID link)
				{
					this.link = link;
					return this;
				}
				
				public LinkType getType()
				{
					return type;
				}
				
				public void transfer()
				{
					if(type.equals(LinkType.NORMAL))
					{
						resolvePane(link).show();
					}
					
					else if(type.equals(LinkType.HOME))
					{
						resolvePane(defaultPane).show();
					}
				}
			}
			
			public abstract class Trigger implements Runnable
			{
				private Sound sound;
				private Float volume;
				private Float pitch;
				private TriggerType type;
				
				public Trigger(TriggerType type)
				{
					this.type = type;
					
					triggers.add(this);
				}
				
				public Player getPlayer()
				{
					return player;
				}
				
				public Sound getSound()
				{
					return sound;
				}
				
				public void setSound(Sound sound)
				{
					this.sound = sound;
				}
				
				public float getVolume()
				{
					if(volume == null)
					{
						return 0f;
					}
					
					return volume;
				}
				
				public void setVolume(float volume)
				{
					this.volume = volume;
				}
				
				public float getPitch()
				{
					if(pitch == null)
					{
						return 0f;
					}
					
					return pitch;
				}
				
				public void setPitch(float pitch)
				{
					this.pitch = pitch;
				}
				
				public void close()
				{
					getPlayer().closeInventory();
				}
				
				public TriggerType getType()
				{
					return type;
				}
				
				public void setType(TriggerType type)
				{
					this.type = type;
				}
				
				public void setVolume(Float volume)
				{
					this.volume = volume;
				}
				
				public void setPitch(Float pitch)
				{
					this.pitch = pitch;
				}
				
				public String toString()
				{
					return "TRIGGER: " + type.toString().toLowerCase() + " SOUND: " + sound + " PITCH: " + getPitch() + " VOLUME: " + getVolume();
				}
				
				public void run()
				{
					if(sound != null)
					{
						if(volume == null)
						{
							volume = new Float(0.4f);
						}
						
						if(pitch == null)
						{
							pitch = new Float(1.0f);
						}
						
						getPlayer().playSound(getPlayer().getLocation(), sound, volume, pitch);
					}
					
					run();
				}
			}
		}
	}
}
