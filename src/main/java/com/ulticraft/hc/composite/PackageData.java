package com.ulticraft.hc.composite;

import com.ulticraft.hc.uapi.Table;

public class PackageData implements Table
{
	private String name;
	private Integer cost;
	private String grant;
	private String initial;
	
	public PackageData(String name, Integer cost, String grant, String initial)
	{
		this.name = name;
		this.cost = cost;
		this.grant = grant;
		this.initial = initial;
	}
	
	public PackageData()
	{
		
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getCost()
	{
		return cost;
	}

	public void setCost(Integer cost)
	{
		this.cost = cost;
	}

	public String getGrant()
	{
		return grant;
	}

	public void setGrant(String grant)
	{
		this.grant = grant;
	}

	public String getInitial()
	{
		return initial;
	}

	public void setInitial(String initial)
	{
		this.initial = initial;
	}
}
