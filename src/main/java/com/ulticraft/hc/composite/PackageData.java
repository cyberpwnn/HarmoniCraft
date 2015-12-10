package com.ulticraft.hc.composite;

import com.ulticraft.hc.uapi.Table;

public class PackageData implements Table
{
	private String name;
	private Integer cost;
	private String material;
	private String grant;
	private String initial;
	private String description;
	
	public PackageData(String name, String description, Integer cost, String grant, String initial, String material)
	{
		this.name = name;
		this.cost = cost;
		this.grant = grant;
		this.initial = initial;
		this.material = material;
		this.description = description;
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

	public String getMaterial()
	{
		return material;
	}

	public void setMaterial(String material)
	{
		this.material = material;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
