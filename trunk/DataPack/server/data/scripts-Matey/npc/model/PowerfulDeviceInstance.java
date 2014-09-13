package npc.model;

import l2next.commons.util.Rnd;
import l2next.gameserver.instancemanager.AwakingManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.ClassId;
import l2next.gameserver.model.base.ClassLevel;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.ExChangeToAwakenedClass;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.ItemFunctions;

public class PowerfulDeviceInstance extends NpcInstance
{
	private static final long serialVersionUID = 8836489477695730511L;

	private int sp = Rnd.get(10000000);
	int NextClassId = 0;
	//private final int MY_CLASS_ID;

	public PowerfulDeviceInstance(int objectId, NpcTemplate template)
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

		if(command.equalsIgnoreCase("Awaken"))
		{
			int essencesCount = AwakingManager.getInstance().giveGiantEssences(player, true);
			NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
			htmlMessage.replace("%SP%", String.valueOf(sp));
			htmlMessage.replace("%ESSENCES%", String.valueOf(essencesCount));

			htmlMessage.setFile("default/" + getNpcId() + "-4.htm");
			player.sendPacket(htmlMessage);
		}
		else if(command.equalsIgnoreCase("Awaken1"))
		{
			NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
			htmlMessage.setFile("awaken/" + player.getClassId().getId() + ".htm");
			player.sendPacket(htmlMessage);
		}
		else if(command.equalsIgnoreCase("Awaken2"))
		{
			int NextClassId = 0;
			switch(player.getClassId().getId())
			{
				case 88:
					NextClassId = 152;
					break;
				case 89:
					NextClassId = 153;
					break;
				case 113:
					NextClassId = 154;
					break;
				case 114:
					NextClassId = 155;
					break;
				case 118:
					NextClassId = 156;
					break;
				case 131:
					NextClassId = 157;
					break;
				case 90:
					NextClassId = 148;
					break;
				case 91:
					NextClassId = 149;
					break;
				case 99:
					NextClassId = 150;
					break;
				case 106:
					NextClassId = 151;
					break;
				case 93:
					NextClassId = 158;
					break;
				case 101:
					NextClassId = 159;
					break;
				case 108:
					NextClassId = 160;
					break;
				case 117:
					NextClassId = 161;
					break;
				case 92:
					NextClassId = 162;
					break;
				case 102:
					NextClassId = 163;
					break;
				case 109:
					NextClassId = 164;
					break;
				case 134:
					NextClassId = 165;
					break;
				case 94:
					NextClassId = 166;
					break;
				case 95:
					NextClassId = 167;
					break;
				case 103:
					NextClassId = 168;
					break;
				case 110:
					NextClassId = 169;
					break;
				case 132:
					NextClassId = 170;
					break;
				case 133:
					NextClassId = 170;
					break;
				case 98:
					NextClassId = 171;
					break;
				case 100:
					NextClassId = 172;
					break;
				case 115:
					NextClassId = 174;
					break;
				case 116:
					NextClassId = 175;
					break;
				case 107:
					NextClassId = 173;
					break;
				//case 136:
				//NextClassId = 176); без перерождения

				case 96:
					NextClassId = 176;
					break;
				case 104:
					NextClassId = 177;
					break;
				case 111:
					NextClassId = 178;
					break;
				case 97:
					NextClassId = 179;
					break;
				case 105:
					NextClassId = 180;
					break;
				case 112:
					NextClassId = 181;
					break;
			}
			player.setVar("AwakenPrepared", "true", -1);
			player.setVar("AwakenedID", NextClassId, -1);
			player.sendPacket(new ExChangeToAwakenedClass(NextClassId));
			player.addExpAndSp(0, sp);
			AwakingManager.getInstance().giveGiantEssences(player, false);
			ItemFunctions.removeItem(player, 17600, 1, true);
			ItemFunctions.addItem(player, 32778, 1, true);
		}
	}

	@Override
	public void showChatWindow(Player player, int val, Object... replace)
	{
		String htmlpath;
		if(val == 0)
		{
			if(player.getClassLevel() == 4 && player.getInventory().getCountOf(17600) > 0)
			{
				switch(player.getClassId().getId())
				{
					case 88:
						NextClassId = 152;
						break;
					case 89:
						NextClassId = 153;
						break;
					case 113:
						NextClassId = 154;
						break;
					case 114:
						NextClassId = 155;
						break;
					case 118:
						NextClassId = 156;
						break;
					case 131:
						NextClassId = 157;
						break;
					case 90:
						NextClassId = 148;
						break;
					case 91:
						NextClassId = 149;
						break;
					case 99:
						NextClassId = 150;
						break;
					case 106:
						NextClassId = 151;
						break;
					case 93:
						NextClassId = 158;
						break;
					case 101:
						NextClassId = 159;
						break;
					case 108:
						NextClassId = 160;
						break;
					case 117:
						NextClassId = 161;
						break;
					case 92:
						NextClassId = 162;
						break;
					case 102:
						NextClassId = 163;
						break;
					case 109:
						NextClassId = 164;
						break;
					case 134:
						NextClassId = 165;
						break;
					case 94:
						NextClassId = 166;
						break;
					case 95:
						NextClassId = 167;
						break;
					case 103:
						NextClassId = 168;
						break;
					case 110:
						NextClassId = 169;
						break;
					case 132:
						NextClassId = 170;
						break;
					case 133:
						NextClassId = 170;
						break;
					case 98:
						NextClassId = 171;
						break;
					case 100:
						NextClassId = 172;
						break;
					case 115:
						NextClassId = 174;
						break;
					case 116:
						NextClassId = 175;
						break;
					case 107:
						NextClassId = 173;
						break;
					case 96:
						NextClassId = 176;
						break;
					case 104:
						NextClassId = 177;
						break;
					case 111:
						NextClassId = 178;
						break;
					case 97:
						NextClassId = 179;
						break;
					case 105:
						NextClassId = 180;
						break;
					case 112:
						NextClassId = 181;
						break;
				}
				int classId = 0;
				for(ClassId classId1 : ClassId.VALUES)
				{
					if(player.getClassId().getClassLevel() == ClassLevel.Fourth && classId1.childOf(player.getClassId()))
					{
						classId = classId1.getId();
						break;
					}
				}
				if(player.getPets().size() > 0)
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				//else if(classId != NextClassId)
				//{
				//	htmlpath = getHtmlPath(getNpcId(), 2, player);
				//}
				else
				{
					if(player.getVar("AwakenedOldIDClass") == null)
					{
						player.setVar("AwakenedOldIDClass", player.getClassId().getId(), -1);
					}
					htmlpath = getHtmlPath(getNpcId(), 3, player);
				}

				if(player.getVarB("AwakenPrepared", false))
				{
					player.sendPacket(new ExChangeToAwakenedClass(NextClassId));
					return;
				}
			}
			else
			{
				htmlpath = getHtmlPath(getNpcId(), val, player);
			}
		}
		else
		{
			htmlpath = getHtmlPath(getNpcId(), val, player);
		}
		showChatWindow(player, htmlpath, replace);

	}

	  private boolean classSynk(Player player)
  {
    int oldId = player.getClassId().getId();
    switch (getNpcId())
    {
    case 33397: 
      if ((oldId == 90) || (oldId == 91) || (oldId == 99) || (oldId == 106)) {
        return true;
      }
      break;
    case 33398: 
      if ((oldId == 88) || (oldId == 89) || (oldId == 113) || (oldId == 114) || (oldId == 118) || (oldId == 131)) {
        return true;
      }
      break;
    case 33399: 
      if ((oldId == 93) || (oldId == 101) || (oldId == 108) || (oldId == 117)) {
        return true;
      }
      break;
    case 33400: 
      if ((oldId == 92) || (oldId == 102) || (oldId == 109) || (oldId == 134)) {
        return true;
      }
      break;
    case 33401: 
      if ((oldId == 94) || (oldId == 95) || (oldId == 103) || (oldId == 110) || (oldId == 132) || (oldId == 133)) {
        return true;
      }
      break;
    case 33402: 
      if ((oldId == 98) || (oldId == 116) || (oldId == 115) || (oldId == 100) || (oldId == 107) || (oldId == 136)) {
        return true;
      }
      break;
    case 33403: 
      if ((oldId == 96) || (oldId == 104) || (oldId == 111)) {
        return true;
      }
      break;
    case 33404: 
      if ((oldId == 97) || (oldId == 105) || (oldId == 112)) {
        return true;
      }
      break;
    }
    return false;
  }
}