package com.ulticraft.hc;

import java.util.Collection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.ulticraft.hc.component.CommandComponent;
import com.ulticraft.hc.component.DataComponent;
import com.ulticraft.hc.component.MessageComponent;
import com.ulticraft.hc.component.NoteComponent;
import com.ulticraft.hc.component.PackageComponent;
import com.ulticraft.hc.component.UIComponent;
import com.ulticraft.hc.composite.PlayerData;
import com.ulticraft.hc.uapi.ComponentManager;
import com.ulticraft.hc.uapi.Dispatcher;

public class HarmoniCraft extends JavaPlugin
{
	private Dispatcher dispatcher;
	private ComponentManager componentManager;
	private CommandComponent commandComponent;
	private MessageComponent messageComponent;
	private NoteComponent noteComponent;
	private DataComponent dataComponent;
	private PackageComponent packageComponent;
	private UIComponent uiComponent;
	
	public void onEnable()
	{
		this.dispatcher = new Dispatcher(this);
		this.componentManager = new ComponentManager(this);
		
		this.commandComponent = new CommandComponent(this);
		this.noteComponent = new NoteComponent(this);
		this.messageComponent = new MessageComponent(this);
		this.dataComponent = new DataComponent(this);
		this.packageComponent = new PackageComponent(this);
		this.uiComponent = new UIComponent(this);
		
		componentManager.register(messageComponent);
		componentManager.register(commandComponent);
		componentManager.register(noteComponent);
		componentManager.register(dataComponent);
		componentManager.register(packageComponent);
		componentManager.register(uiComponent);
		
		componentManager.enable();
		
		packageComponent.load();
	}
	
	public void onDisable()
	{
		componentManager.disable();
	}
	
	public void msg(CommandSender sender, String msg)
	{
		sender.sendMessage(msg);
	}
	
	public void msg(CommandSender sender, String[] msgs)
	{
		for(String i : msgs)
		{
			msg(sender, i);
		}
	}
	
	public PlayerData gpd(Player p)
	{
		return getDataComponent().get(p);
	}
	
	public int scheduleSyncRepeatingTask(int delay, int interval, Runnable runnable)
	{
		return getServer().getScheduler().scheduleSyncRepeatingTask(this, runnable, delay, interval);
	}
	
	public int scheduleSyncTask(int delay, Runnable runnable)
	{
		return getServer().getScheduler().scheduleSyncDelayedTask(this, runnable, delay);
	}
	
	public void cancelTask(int tid)
	{
		getServer().getScheduler().cancelTask(tid);
	}
	
	public Collection<? extends Player> onlinePlayers()
	{
		return getServer().getOnlinePlayers();
	}
	
	public boolean canFindPlayer(String search)
	{
		return findPlayer(search) == null ? false : true;
	}
	
	public Player findPlayer(String search)
	{
		for(Player i : onlinePlayers())
		{
			if(i.getName().equalsIgnoreCase(search))
			{
				return i;
			}
		}
		
		for(Player i : onlinePlayers())
		{
			if(i.getName().toLowerCase().contains(search.toLowerCase()))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public CommandComponent getCommandComponent()
	{
		return commandComponent;
	}

	public MessageComponent getMessageComponent()
	{
		return messageComponent;
	}
	
	public NoteComponent getNoteComponent()
	{
		return noteComponent;
	}

	public DataComponent getDataComponent()
	{
		return dataComponent;
	}

	public UIComponent getUiComponent()
	{
		return uiComponent;
	}

	public void register(Listener listener)
	{
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	public void unRegister(Listener listener)
	{
		HandlerList.unregisterAll(listener);
	}
	
	public PackageComponent getPackageComponent()
	{
		return packageComponent;
	}

	public ComponentManager getComponentManager()
	{
		return componentManager;
	}

	public Dispatcher getDispatcher()
	{
		return dispatcher;
	}
	
	public void i(String... o)
	{
		dispatcher.info(o);
	}
	
	public void s(String... o)
	{
		dispatcher.success(o);
	}
	
	public void f(String... o)
	{
		dispatcher.failure(o);
	}
	
	public void w(String... o)
	{
		dispatcher.warning(o);
	}
	
	public void v(String... o)
	{
		dispatcher.verbose(o);
	}
	
	public void o(String... o)
	{
		dispatcher.overbose(o);
	}
	
	public void si(String... o)
	{
		dispatcher.sinfo(o);
	}
	
	public void ss(String... o)
	{
		dispatcher.ssuccess(o);
	}
	
	public void sf(String... o)
	{
		dispatcher.sfailure(o);
	}
	
	public void sw(String... o)
	{
		dispatcher.swarning(o);
	}
	
	public void sv(String... o)
	{
		dispatcher.sverbose(o);
	}
	
	public void so(String... o)
	{
		dispatcher.soverbose(o);
	}
}
