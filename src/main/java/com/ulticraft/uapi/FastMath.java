package com.ulticraft.uapi;

import org.bukkit.Location;

public class FastMath
{
	public static boolean isInRadius(Location a, Location b, float f)
	{
		return a.distanceSquared(b) <= f * f;
	}
}
