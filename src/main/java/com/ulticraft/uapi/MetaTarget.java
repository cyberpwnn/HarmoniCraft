package com.ulticraft.uapi;

import java.io.File;
import org.apache.commons.lang3.StringUtils;

public class MetaTarget
{
	private File path;
	
	public MetaTarget(String path)
	{
		this.path = new File(path);
	}
	
	public MetaTarget(MetaTarget target, String path)
	{
		this.path = new File(target.getPath(), path);
	}
	
	public String getPath()
	{
		return path.getPath();
	}
	
	public String getPathYAML()
	{
		String s = path.getPath();
		
		s = StringUtils.replace(s, File.separator, ".");
		return s.substring(0, 1).equals(".") ? s.substring(1) : s;
	}
	
	public MetaTarget getParent()
	{
		if(path.getParentFile() == null)
		{
			return null;
		}
		
		return new MetaTarget(path.getParentFile().getPath());
	}
	
	public boolean hasParent()
	{
		return getParent() != null;
	}
	
	public String[] split()
	{
		UList<String> sms = new UList<String>();
		MetaTarget mt = new MetaTarget(getPath());
		
		while(mt.hasParent())
		{
			sms.add(new File(mt.getPath()).getName());
			mt = mt.getParent();
		}
		
		return sms.reverse().toArray(new String[sms.size()]);
	}
	
	public String getName()
	{
		return path.getName();
	}
	
	@Override
	public String toString()
	{
		return path.getPath();
	}
}
