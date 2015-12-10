package com.ulticraft.hc.component;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.composite.PackageData;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.Depend;
import com.ulticraft.hc.uapi.Gui;
import com.ulticraft.hc.uapi.Gui.Pane;
import com.ulticraft.hc.uapi.Gui.Pane.Element;

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
		if(Material.valueOf(pd.getMaterial()) != null)
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
		
		for(final PackageData i : pl.getPackageComponent().get())
		{
			Element element = pane.new Element(i.getName(), getMaterial(i), j);
			element.addBullet(i.getDescription());
			element.addInfo("Costs " + i.getCost() + " Notes");
			
			if(pl.getNoteComponent().has(p, i.getCost()))
			{
				element.setRequirement("Affordable!");
				element.setQuickRunnable(new Runnable()
				{
					@Override
					public void run()
					{
						gui.close();
						pane.breakElements();
						pl.getPackageComponent().aquire(p, i);
					}
				});
			}
			
			else
			{
				element.setFailedRequirement("Not Enough Notes!");
			}
			
			j++;
		}
		
		pane.setDefault();
		gui.show();
	}
}
