package com.ulticraft.hc.uapi;

public class MetaData
{
	private String root;
	private MetaReference[] references;
	
	public MetaData(String root)
	{
		this.root = root;
	}

	public String getRoot()
	{
		return root;
	}

	public void setRoot(String root)
	{
		this.root = root;
	}

	public MetaReference[] getReferences()
	{
		return references;
	}

	public void setReferences(MetaReference[] references)
	{
		this.references = references;
	}
}
