package com.ulticraft.uapi;

public class MetaReference
{
	private MetaTarget target;
	private Class<?> type;
	
	public MetaReference(MetaTarget target, Class<?> type)
	{
		this.target = target;
		this.type = type;
	}
	
	public MetaTarget getTarget()
	{
		return target;
	}
	
	public void setTarget(MetaTarget target)
	{
		this.target = target;
	}
	
	public Class<?> getType()
	{
		return type;
	}
	
	public void setType(Class<?> type)
	{
		this.type = type;
	}
	
	@Override
	public String toString()
	{
		return target.getPath() + ": " + type.getSimpleName();
	}
}
