package com.ulticraft.uapi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.lang.StringUtils;
import com.ulticraft.hc.HarmoniCraft;

public class ComponentManager
{
	private UMap<Object, Boolean> components;
	private UList<Object> order;
	private HarmoniCraft pl;
	
	public ComponentManager(HarmoniCraft pl)
	{
		components = new UMap<Object, Boolean>();
		this.pl = pl;
	}
	
	public void register(Object o)
	{
		components.put(o, false);
	}
	
	public void enable()
	{
		order = new UList<Object>();
		
		pl.v("Enabling Components");
		
		for(Object i : components.keySet())
		{
			enable(i);
		}
	}
	
	public void disable()
	{
		pl.v("Disabling Components");
		
		for(int i = order.size() - 1; i >= 0; i--)
		{
			disable(order.get(i));
		}
	}
	
	public void enable(Object c)
	{
		if(isValid(c))
		{
			Class<?> clazz = c.getClass();
			Depend[] dependencies = clazz.getDeclaredAnnotationsByType(Depend.class);
			Method enable = getDeclaredMethod(c, "enable");
			Boolean enabled = components.get(c);
			
			if(enabled == null || enabled)
			{
				return;
			}
			
			for(Depend i : dependencies)
			{
				for(Class<?> j : i.value())
				{
					Object dc = resolve(j);
					
					if(dc != null)
					{
						enable(dc);
					}
					
					else
					{
						continue;
					}
				}
			}
			
			if(enable != null)
			{
				try
				{
					enable.invoke(c);
					components.put(c, true);
					order.add(c);
					pl.v("Component " + StringUtils.remove(c.getClass().getSimpleName(), "Component") + " Enabled");
				}
				
				catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					return;
				}
			}
		}
	}
	
	public void disable(Object c)
	{
		if(isValid(c))
		{
			Method disable = getDeclaredMethod(c, "disable");
			
			if(components.get(c) && disable != null)
			{
				try
				{
					disable.invoke(c);
					components.put(c, false);
					pl.v("Component " + StringUtils.remove(c.getClass().getSimpleName(), "Component") + " Disabled");
				}
				
				catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					return;
				}
			}
		}
	}
	
	private Object resolve(Class<?> clazz)
	{
		for(Object i : components.keySet())
		{
			if(i.getClass().equals(clazz))
			{
				return i;
			}
		}
		
		return null;
	}
	
	private boolean isValid(Object o)
	{
		return o.getClass().getSuperclass().equals(Component.class) && components.containsKey(o);
	}
	
	private Method getDeclaredMethod(Object o, String mn)
	{
		try
		{
			return o.getClass().getDeclaredMethod(mn);
		}
		
		catch(NoSuchMethodException | SecurityException e)
		{
			return null;
		}
	}
}
