package npc.model;

import l2next.commons.util.Rnd;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.ExChangeToAwakenedClass;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.templates.npc.NpcTemplate;

public class HardinInstance extends NpcInstance
{
	private static final long serialVersionUID = 8836489477695730511L;

	private int sp = Rnd.get(10000000);
	int NextClassId = 0;
	//private final int MY_CLASS_ID;

	public HardinInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		//MY_CLASS_ID = getParameter("MyClassId", -1);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}

		if(command.equalsIgnoreCase("AwakenChaos"))
		{
			//ItemFunctions.removeItem(player, SCROLL_OF_AFTERLIFE, 1, true);
			NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
			/**
			 Awakeninger:
			 Система фактического перебора старого/нового/линдвиоровского класса
			 */
			switch(player.getClassId().getId())
			{
				case 139:
				{
					htmlMessage.setFile("default/" + getNpcId() + "-Knight.htm");
					break;
				}
				case 140:
				{
					htmlMessage.setFile("default/" + getNpcId() + "-Warrior.htm");
					break;
				}
				case 141:
				{
					htmlMessage.setFile("default/" + getNpcId() + "-Rogue.htm");
					break;
				}
				case 142:
				{
					htmlMessage.setFile("default/" + getNpcId() + "-Archer.htm");
					break;
				}
				case 143:
				{
					htmlMessage.setFile("default/" + getNpcId() + "-Wizard.htm");
					break;
				}
				case 144:
				{
					htmlMessage.setFile("default/" + getNpcId() + "-Enchanter.htm");
					break;
				}
				case 145:
				{
					htmlMessage.setFile("default/" + getNpcId() + "-Summoner.htm");
					break;
				}
				case 146:
				{
					htmlMessage.setFile("default/" + getNpcId() + "-Healer.htm");
					break;
				}
			}
			player.sendPacket(htmlMessage);
		}
		if(command.equalsIgnoreCase("Knight1"))
		{
			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 148, -1);
			player.sendPacket(new ExChangeToAwakenedClass(148));
		}
		if(command.equalsIgnoreCase("Knight2"))
		{
			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 149, -1);
			player.sendPacket(new ExChangeToAwakenedClass(149));
		}
		if(command.equalsIgnoreCase("Knight3"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 150, -1);
			player.sendPacket(new ExChangeToAwakenedClass(150));
		}
		if(command.equalsIgnoreCase("Knight4"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 151, -1);
			player.sendPacket(new ExChangeToAwakenedClass(151));
		}
		if(command.equalsIgnoreCase("Warrior1"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 152, -1);
			player.sendPacket(new ExChangeToAwakenedClass(152));
		}
		if(command.equalsIgnoreCase("Warrior2"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 153, -1);
			player.sendPacket(new ExChangeToAwakenedClass(153));
		}
		if(command.equalsIgnoreCase("Warrior3"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 154, -1);
			player.sendPacket(new ExChangeToAwakenedClass(154));
		}
		if(command.equalsIgnoreCase("Warrior4"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 155, -1);
			player.sendPacket(new ExChangeToAwakenedClass(155));
		}
		if(command.equalsIgnoreCase("Warrior5"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 156, -1);
			player.sendPacket(new ExChangeToAwakenedClass(156));
		}
		if(command.equalsIgnoreCase("Warrior6"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 157, -1);
			player.sendPacket(new ExChangeToAwakenedClass(157));
		}
		if(command.equalsIgnoreCase("Rogue1"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 158, -1);
			player.sendPacket(new ExChangeToAwakenedClass(158));
		}
		if(command.equalsIgnoreCase("Rogue2"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 159, -1);
			player.sendPacket(new ExChangeToAwakenedClass(159));
		}
		if(command.equalsIgnoreCase("Rogue3"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 160, -1);
			player.sendPacket(new ExChangeToAwakenedClass(160));
		}
		if(command.equalsIgnoreCase("Rogue4"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 161, -1);
			player.sendPacket(new ExChangeToAwakenedClass(161));
		}
		if(command.equalsIgnoreCase("Archer1"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 162, -1);
			player.sendPacket(new ExChangeToAwakenedClass(162));
		}
		if(command.equalsIgnoreCase("Archer2"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 163, -1);
			player.sendPacket(new ExChangeToAwakenedClass(163));
		}
		if(command.equalsIgnoreCase("Archer3"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 164, -1);
			player.sendPacket(new ExChangeToAwakenedClass(164));
		}
		if(command.equalsIgnoreCase("Archer4"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 165, -1);
			player.sendPacket(new ExChangeToAwakenedClass(165));
		}
		if(command.equalsIgnoreCase("Wizard1"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 166, -1);
			player.sendPacket(new ExChangeToAwakenedClass(166));
		}
		if(command.equalsIgnoreCase("Wizard2"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 167, -1);
			player.sendPacket(new ExChangeToAwakenedClass(167));
		}
		if(command.equalsIgnoreCase("Wizard3"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 168, -1);
			player.sendPacket(new ExChangeToAwakenedClass(168));
		}
		if(command.equalsIgnoreCase("Wizard4"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 169, -1);
			player.sendPacket(new ExChangeToAwakenedClass(169));
		}
		if(command.equalsIgnoreCase("Wizard5"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 170, -1);
			player.sendPacket(new ExChangeToAwakenedClass(170));
		}
		if(command.equalsIgnoreCase("Enchanter1"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 171, -1);
			player.sendPacket(new ExChangeToAwakenedClass(171));
		}
		if(command.equalsIgnoreCase("Enchanter2"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 172, -1);
			player.sendPacket(new ExChangeToAwakenedClass(172));
		}
		if(command.equalsIgnoreCase("Enchanter3"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 173, -1);
			player.sendPacket(new ExChangeToAwakenedClass(173));
		}
		if(command.equalsIgnoreCase("Enchanter4"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 174, -1);
			player.sendPacket(new ExChangeToAwakenedClass(174));
		}
		if(command.equalsIgnoreCase("Enchanter5"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 175, -1);
			player.sendPacket(new ExChangeToAwakenedClass(175));
		}
		if(command.equalsIgnoreCase("Summoner1"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 176, -1);
			player.sendPacket(new ExChangeToAwakenedClass(176));
		}
		if(command.equalsIgnoreCase("Summoner2"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 177, -1);
			player.sendPacket(new ExChangeToAwakenedClass(177));
		}
		if(command.equalsIgnoreCase("Summoner3"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 178, -1);
			player.sendPacket(new ExChangeToAwakenedClass(178));
		}
		if(command.equalsIgnoreCase("Healer1"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 179, -1);
			player.sendPacket(new ExChangeToAwakenedClass(179));
		}
		if(command.equalsIgnoreCase("Healer2"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 180, -1);
			player.sendPacket(new ExChangeToAwakenedClass(180));
		}
		if(command.equalsIgnoreCase("Healer3"))
		{

			player.setVar("AwakenPreparedChaos", "true", -1);
			player.setVar("AwakenedIDChaos", 181, -1);
			player.sendPacket(new ExChangeToAwakenedClass(181));
		}
	}
}