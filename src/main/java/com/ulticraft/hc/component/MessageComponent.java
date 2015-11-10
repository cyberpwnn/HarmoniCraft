package com.ulticraft.hc.component;

import java.io.File;
import java.io.IOException;
import com.ulticraft.hc.HarmoniCraft;
import com.ulticraft.hc.composite.MessageData;
import com.ulticraft.hc.uapi.Component;
import com.ulticraft.hc.uapi.DataManager;
import com.ulticraft.hc.uapi.Depend;

@Depend(DataComponent.class)
public class MessageComponent extends Component
{
	private File base;
	private MessageData messageData;
	
	public MessageComponent(HarmoniCraft pl)
	{
		super(pl);
		
		base = new File(pl.getDataFolder(), "messages.yml");
	}
	
	public void enable()
	{
		DataManager dm = new DataManager(pl, base);
		
		if(base.exists())
		{
			messageData = (MessageData) dm.readYAML(MessageData.class);
		}
		
		else
		{
			try
			{
				base.createNewFile();
				messageData = new MessageData();
				messageData.setDefaults();
				dm.writeYAML(messageData);
			}
			
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void disable()
	{
		DataManager dm = new DataManager(pl, base);
		
		if(base.exists())
		{
			dm.writeYAML(messageData);
		}
	}
	
	public MessageData getMessageData()
	{
		return messageData;
	}
}
