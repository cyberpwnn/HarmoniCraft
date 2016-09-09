package com.ulticraft.hc.component;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.composite.PackageData;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.Depend;
import com.ulticraft.hc.uapi.Gui;
import com.ulticraft.hc.uapi.Gui.Pane;
import com.ulticraft.hc.uapi.Gui.Pane.Element;
import net.md_5.bungee.api.ChatColor;

@Depend(PackageComponent.class)
public class UIComponent extends Component
{
	public UIComponent(HarmoniCraft pl)
	{
		super(pl);
	}
	
	public void enable()
	{
		
	}
	
	public void disable()
	{
		
	}
	
	public Material getMaterial(PackageData pd)
	{
		if (Material.valueOf(pd.getMaterial()) != null)
		{
			return Material.valueOf(pd.getMaterial());
		}
		
		else
		{
			return Material.NOTE_BLOCK;
		}
	}
	
	public void open(Player p)
	{
		
	}
	
	public void openBuy(final Player p)
	{
		final Gui gui = new Gui(p, pl);
		final Pane pane = gui.new Pane("Note Shop");
		
		int j = 0;
		
		for (final PackageData i : pl.getPackageComponent().get())
		{
			if (pl.getPackageComponent().has(p, i))
			{
				continue;
			}
			
			Element element = pane.new Element(ChatColor.YELLOW + i.getName(), getMaterial(i), j);
			element.resetDescription();
			element.addInfo(ChatColor.GOLD + "Costs " + i.getCost() + " Notes");
			
			for (String k : StringUtils.split(WordUtils.wrap(i.getDescription(), 48), '\n'))
			{
				element.addInfo(k.trim());
			}
			
			if (i.getReoccurring() != null && i.getReoccurring() == true)
			{
				element.addRequirement("This item can be purchased multiple times");
			}
			
			else
			{
				element.addRequirement("This item is a one-time purchase");
			}
			
			if (pl.getNoteComponent().has(p, i.getCost()))
			{
				element.addRequirement("You can afford this item!");
			}
			
			else
			{
				element.addFailedRequirement("You can't afford this item!");
			}
			
			for (String k : i.getDependencies())
			{
				if (pl.getPackageComponent().get(k) != null)
				{
					if (pl.getPackageComponent().has(p, k))
					{
						element.addRequirement("Requires " + k);
					}
					
					else
					{
						element.addFailedRequirement("Requires " + k);
					}
				}
			}
			
			element.setQuickRunnable(new Runnable()
			{
				@Override
				public void run()
				{
					if (pl.gpd(p).getNotes() >= i.getCost())
					{
						for (String k : i.getDependencies())
						{
							if (pl.getPackageComponent().get(k) != null)
							{
								if (!pl.getPackageComponent().has(p, k))
								{
									return;
								}
							}
							
							else
							{
								pl.f("INVALID PACKAGE REQUIREMENT: " + i.getName() + " requires ??? (" + k + ") ???");
							}
						}
						
						gui.close();
						pl.getPackageComponent().aquire(p, i);
					}
				}
			});
							
			j++;
		}
		
		pane.setDefault();
		gui.show();
	}
	
	public void openOwned(final Player p)
	{
		final Gui gui = new Gui(p, pl);
		final Pane pane = gui.new Pane("Owned Packages");
		
		int j = 0;
		
		for (final PackageData i : pl.getPackageComponent().get())
		{
			if (!pl.getPackageComponent().has(p, i))
			{
				continue;
			}
			
			Element element = pane.new Element(ChatColor.YELLOW + i.getName(), getMaterial(i), j);
			element.resetDescription();
			element.addInfo(WordUtils.wrap(i.getDescription(), 32));
			
			j++;
		}
		
		pane.setDefault();
		gui.show();
	}
}
