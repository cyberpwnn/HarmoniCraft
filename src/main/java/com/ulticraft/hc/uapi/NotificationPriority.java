package com.ulticraft.hc.uapi;

public enum NotificationPriority
{
	LOWEST(0),
	LOW(1),
	MEDIUM(2),
	HIGH(3),
	HIGHEST(4);
	
	private final int priority;
	
	NotificationPriority(int priority)
	{
		this.priority = priority;
	}

	public int getPriority()
	{
		return priority;
	}
}
