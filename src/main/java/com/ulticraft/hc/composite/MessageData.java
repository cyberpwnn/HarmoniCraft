package com.ulticraft.hc.composite;

import com.ulticraft.hc.uapi.Table;

public class MessageData implements Table
{
	private String notesGet;
	private String notesGot;
	
	public MessageData()
	{
		
	}
	
	public void setDefaults()
	{
		notesGet = "You have %s Notes!";
		notesGet = "You earned %s Notes!";
	}

	public String getNotesGet()
	{
		return notesGet;
	}

	public void setNotesGet(String notesGet)
	{
		this.notesGet = notesGet;
	}

	public String getNotesGot()
	{
		return notesGot;
	}

	public void setNotesGot(String notesGot)
	{
		this.notesGot = notesGot;
	}
}
