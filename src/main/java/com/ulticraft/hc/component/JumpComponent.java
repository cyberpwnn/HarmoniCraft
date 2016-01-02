package com.ulticraft.hc.component;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.Info;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.UList;
import com.ulticraft.hc.uapi.UMap;

public class JumpComponent extends Component implements Listener
{
	private UList<Player> players;
	private UList<Player> fixed;
	private UMap<Player, Boolean> cooldown;
	
	public JumpComponent(HarmoniCraft pl)
	{
		super(pl);
		
		pl.register(this);
		
		fixed = new UList<Player>();
		players = new UList<Player>();
		cooldown = new UMap<Player, Boolean>();
	}
	
	@Override
	public void enable()
	{
		super.enable();
	}
	
	@Override
	public void disable()
	{
		super.disable();
	}
	
	public UList<Player> getPlayers()
	{
		return players;
	}
	
	public void setPlayers(UList<Player> players)
	{
		this.players = players;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		
		if(!p.hasPermission(Info.PERM_GOD) && p.getAllowFlight() == true && !fixed.contains(p))
		{
			p.setFlying(false);
			p.setAllowFlight(false);
			fixed.add(p);
		}
		
		if(p.getGameMode() == GameMode.CREATIVE)
			return;
		if(!p.hasPermission(Info.PERM_GOD))
			return;
			
		if(cooldown.get(p) != null && cooldown.get(p) == true)
		{
			p.setAllowFlight(true);
		}
		else
		{
			p.setAllowFlight(false);
		}
		
		if(p.isOnGround())
		{
			cooldown.put(p, true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFly(PlayerToggleFlightEvent e)
	{
		Player p = e.getPlayer();
		
		if(p.getGameMode() == GameMode.CREATIVE)
			return;
		if(p.hasPermission(Info.PERM_GOD))
		{
			if(cooldown.get(p) == true)
			{
				e.setCancelled(true);
				cooldown.put(p, false);
				p.setVelocity(p.getLocation().getDirection().multiply(1.6D).setY(1.0D));
				
				p.setAllowFlight(false);
			}
		}
	}
}
