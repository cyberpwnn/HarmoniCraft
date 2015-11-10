package com.ulticraft.hc.uapi;

import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.util.Date;

public class CommonUtil
{
	public static String date()
	{
		return DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
	}
	
	public static String address(InetSocketAddress address)
	{
		byte[] raw = address.getAddress().getAddress();
		UList<Integer> ipBuilder = new UList<Integer>();
		
		for(byte i : raw)
		{
			ipBuilder.add((int)i);
		}
		
		return ipBuilder.toString(".");
	}
}
