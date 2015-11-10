package com.ulticraft.hc.uapi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ClassUtils;
import org.bukkit.ChatColor;

public class Reflector
{
	private UList<String> log;
	
	public Reflector()
	{
		log = new UList<String>();
	}
	
	private void l(String s)
	{
		log.add(s);
	}
	
	private void w(String s)
	{
		l(ChatColor.YELLOW + s);
	}
	
	private void i(String s)
	{
		l(ChatColor.GREEN + s);
	}
	
	public void showLog(Dispatcher d)
	{
		for(String i : log)
		{
			d.overbose(i);
		}
	}
	
	public Object get(Object o, MetaReference reference)
	{
		Object m = o;
		
		for(String i : reference.getTarget().split())
		{
			if(m != null)
			{
				m = get(m, i);
			}
		}
		
		return reference.getType().cast(m);
	}
	
	public void set(Object o, MetaReference reference, Object par)
	{
		Object m = o;
		
		for(String i : reference.getTarget().split())
		{
			if(i.equals(reference.getTarget().split()[reference.getTarget().split().length - 1]))
			{
				set(m, i, par);
				return;
			}
			
			if(m != null)
			{
				m = get(m, i);
			}
		}
	}
	
	private Object get(Object o, String name)
	{
		try
		{
			return getGetter(o.getClass().getDeclaredField(name)).invoke(o);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void set(Object o, String name, Object par)
	{
		try
		{
			getSetter(o.getClass().getDeclaredField(name)).invoke(o, par);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public MetaData getMetaData(Class<?> clazz)
	{
		UList<MetaReference> mr = getMetaReferences(clazz, null);
		MetaData md = new MetaData(clazz.getName());
		md.setReferences(mr.toArray(new MetaReference[mr.size()]));
		return md;
	}
	
	private UList<MetaReference> getMetaReferences(Class<?> clazz, String base)
	{
		UList<MetaReference> references = new UList<MetaReference>();
		UList<Class<?>> used = new UList<Class<?>>();
		
		if(base == null)
		{
			base = "";
		}
		
		if(!isTable(clazz))
		{
			w(clazz.getSimpleName() + " is not a table.");
			return null;
		}
		
		if(used.contains(clazz))
		{
			w(clazz.getSimpleName() + " at " + base + " cannot be inserterted, as it has already been used.");
			return null;
		}
		
		else
		{
			used.add(clazz);
		}
		
		for(Field i : clazz.getDeclaredFields())
		{
			if(isValidField(i))
			{
				if(isSafe(i.getType()))
				{
					MetaReference mr = new MetaReference(new MetaTarget(new MetaTarget(base), i.getName()), i.getType());
					i("Compiling: " + ChatColor.DARK_AQUA + (mr.getTarget().getParent().toString().length() == 1 ? "" : mr.getTarget().getParent().toString()) + ChatColor.AQUA + File.separator + mr.getTarget().getName() + ChatColor.BLUE + " [" + mr.getType().getSimpleName() + "]");
					
					references.add(mr);
				}
				
				else if(isTable(i.getType()))
				{
					UList<MetaReference> iReferences = getMetaReferences(i.getType(), new MetaTarget(new MetaTarget(base), i.getName()).toString());
					if(iReferences != null && !iReferences.isEmpty())
					{
						references.add(iReferences.toArray(new MetaReference[iReferences.size()]));
					}
				}
			}
		}
		
		return references;
	}
	
	public boolean isSafe(Class<?> type)
	{
		if(type.isArray())
		{
			return false;
		}
		
		if(ClassUtils.isPrimitiveWrapper(type))
		{
			return true;
		}
		
		if(type.equals(String.class))
		{
			return true;
		}
		
		if(isTable(type))
		{
			return false;
		}
		
		w("Unsafe Type: " + type.getName());
		
		return false;
	}
	
	public boolean isTable(Class<?> clazz)
	{
		for(Class<?> i : clazz.getInterfaces())
		{
			if(i.equals(Table.class))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isValidField(Field f)
	{
		Method g = getGetter(f);
		Method s = getSetter(f);
		
		if(!Modifier.isPrivate(f.getModifiers()) || Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()))
		{
			w("Invalid feild modifiers: " + f.getDeclaringClass().getSimpleName() + " " + Modifier.toString(f.getModifiers()) + " " + f.getType().getSimpleName() + " " + f.getName());
			return false;
		}
		
		if(g == null || s == null)
		{
			if(g == null)
			{
				w("No Defined Getter: " + f.getDeclaringClass().getSimpleName() + " " + f.getType().getSimpleName() + " " + f.getName());
			}
			
			if(s == null)
			{
				w("No Defined Setter: " + f.getDeclaringClass().getSimpleName() + " " + f.getType().getSimpleName() + " " + f.getName());
			}
			
			return false;
		}
		
		return true;
	}
	
	public Object instantiate(Class<?> clazz)
	{
		try
		{
			return clazz.getDeclaredConstructor().newInstance();
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Object valueOf(MetaReference mr, Object o)
	{
		if(mr.getType().equals(String.class))
		{
			return String.valueOf(o);
		}
		
		if(mr.getType().equals(Character.class))
		{
			return Character.valueOf(o.toString().charAt(0));
		}
		
		if(mr.getType().equals(Integer.class))
		{
			return Integer.valueOf(o.toString());
		}
		
		if(mr.getType().equals(Short.class))
		{
			return Short.valueOf(o.toString());
		}
		
		if(mr.getType().equals(Long.class))
		{
			return Long.valueOf(o.toString());
		}
		
		if(mr.getType().equals(Byte.class))
		{
			return Byte.valueOf(o.toString());
		}
		
		if(mr.getType().equals(Double.class))
		{
			return Double.valueOf(o.toString());
		}
		
		if(mr.getType().equals(Float.class))
		{
			return Float.valueOf(o.toString());
		}
		
		if(mr.getType().equals(Boolean.class))
		{
			return Boolean.valueOf((Boolean) o);
		}
		
		return o;
	}
	
	public Method getGetter(Field f)
	{
		try
		{
			Method m = f.getDeclaringClass().getMethod(getGetterName(f.getName()));
			
			if(m.getReturnType().equals(f.getType()))
			{
				if(!Modifier.isPublic(m.getModifiers()) || Modifier.isStatic(m.getModifiers()) || Modifier.isFinal(m.getModifiers()))
				{
					return null;
				}
				
				return m;
			}
		}
		
		catch(Exception e)
		{
		
		}
		
		try
		{
			Method m = f.getDeclaringClass().getMethod(getIserName(f.getName()));
			
			if(m.getReturnType().equals(f.getType()))
			{
				if(!Modifier.isPublic(m.getModifiers()) || Modifier.isStatic(m.getModifiers()) || Modifier.isFinal(m.getModifiers()))
				{
					return null;
				}
				
				return m;
			}
		}
		
		catch(Exception e)
		{
			
		}
		
		return null;
	}
	
	public Method getSetter(Field f)
	{
		try
		{
			Method m = f.getDeclaringClass().getMethod(getSetterName(f.getName()), f.getType());
			
			if(m.getReturnType().equals(Void.TYPE))
			{
				if(!Modifier.isPublic(m.getModifiers()) || Modifier.isStatic(m.getModifiers()) || Modifier.isFinal(m.getModifiers()))
				{
					return null;
				}
				
				return m;
			}
		}
		
		catch(Exception e)
		{
			w(e.getMessage());
		}
		
		return null;
	}
	
	public String getGetterName(String field)
	{
		return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}
	
	public String getSetterName(String field)
	{
		return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}
	
	public String getIserName(String field)
	{
		return "is" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}
	
	public DataOutputStream writeRespective(DataOutputStream dos, MetaReference mr, Object o)
	{
		try
		{
			if(mr.getType().equals(Integer.class))
			{
				dos.writeInt((Integer) o);
			}
			
			else if(mr.getType().equals(Short.class))
			{
				dos.writeShort((Short) o);
			}
			
			else if(mr.getType().equals(Long.class))
			{
				dos.writeLong((Long) o);
			}
			
			else if(mr.getType().equals(Byte.class))
			{
				dos.writeByte((Byte) o);
			}
			
			else if(mr.getType().equals(Double.class))
			{
				dos.writeDouble((Double) o);
			}
			
			else if(mr.getType().equals(Float.class))
			{
				dos.writeFloat((Float) o);
			}
			
			else if(mr.getType().equals(String.class))
			{
				dos.writeUTF((String) o);
			}
			
			else if(mr.getType().equals(Boolean.class))
			{
				dos.writeBoolean((Boolean) o);
			}
			
			else if(mr.getType().equals(Character.class))
			{
				dos.writeChar((Character) o);
			}
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return dos;
	}
	
	public Object readRespective(DataInputStream dos, MetaReference mr)
	{
		try
		{
			if(mr.getType().equals(Integer.class))
			{
				return dos.readInt();
			}
			
			else if(mr.getType().equals(Short.class))
			{
				return dos.readShort();
			}
			
			else if(mr.getType().equals(Long.class))
			{
				return dos.readLong();
			}
			
			else if(mr.getType().equals(Byte.class))
			{
				return dos.readByte();
			}
			
			else if(mr.getType().equals(Double.class))
			{
				return dos.readDouble();
			}
			
			else if(mr.getType().equals(Float.class))
			{
				return dos.readFloat();
			}
			
			else if(mr.getType().equals(String.class))
			{
				return dos.readUTF();
			}
			
			else if(mr.getType().equals(Boolean.class))
			{
				return dos.readBoolean();
			}
			
			else if(mr.getType().equals(Character.class))
			{
				return dos.readChar();
			}
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
