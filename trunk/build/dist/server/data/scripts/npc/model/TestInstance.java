package npc.model;

import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.*;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.templates.npc.NpcTemplate;

//import instances.CoralGarden;

public class TestInstance extends NpcInstance
{
	public TestInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}

		if(command.equalsIgnoreCase("1"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 1));
		}
		else if(command.equalsIgnoreCase("2"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 2));
		}
		else if(command.equalsIgnoreCase("3"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 3));
		}
		else if(command.equalsIgnoreCase("4"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 4));
		}
		else if(command.equalsIgnoreCase("5"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 5));
		}
		else if(command.equalsIgnoreCase("6"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 6));
		}
		else if(command.equalsIgnoreCase("7"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 7));
		}
		else if(command.equalsIgnoreCase("8"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 8));
		}
		else if(command.equalsIgnoreCase("9"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 9));
		}
		else if(command.equalsIgnoreCase("10"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 10));
		}
		else if(command.equalsIgnoreCase("11"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 11));
		}
		else if(command.equalsIgnoreCase("12"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 12));
		}
		else if(command.equalsIgnoreCase("13"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 13));
		}
		else if(command.equalsIgnoreCase("14"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 14));
		}
		else if(command.equalsIgnoreCase("15"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 15));
		}
		else if(command.equalsIgnoreCase("16"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 16));
		}
		else if(command.equalsIgnoreCase("17"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 17));
		}
		else if(command.equalsIgnoreCase("18"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 18));
		}
		else if(command.equalsIgnoreCase("19"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 19));
		}
		else if(command.equalsIgnoreCase("20"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 20));
		}
		else if(command.equalsIgnoreCase("21"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 21));
		}
		else if(command.equalsIgnoreCase("22"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 22));
		}
		else if(command.equalsIgnoreCase("23"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 23));
		}
		else if(command.equalsIgnoreCase("24"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 24));
		}
		else if(command.equalsIgnoreCase("25"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 25));
		}
		else if(command.equalsIgnoreCase("26"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 26));
		}
		else if(command.equalsIgnoreCase("27"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 27));
		}
		else if(command.equalsIgnoreCase("28"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 28));
		}
		else if(command.equalsIgnoreCase("29"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 29));
		}
		else if(command.equalsIgnoreCase("30"))
		{
			player.broadcastPacket(new SocialAction(player.getObjectId(), 30));
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... replace)
	{
		NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
		htmlMessage.setFile("default/53020.htm");
		player.sendPacket(htmlMessage);
	}
}
