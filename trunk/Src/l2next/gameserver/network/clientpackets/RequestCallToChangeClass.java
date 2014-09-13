package l2next.gameserver.network.clientpackets;

//import l2next.gameserver.instancemanager.AwakingManager;

import l2next.gameserver.Config;
import l2next.gameserver.instancemanager.AwakingManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.ClassId;
import l2next.gameserver.network.serverpackets.ExCallToChangeClass;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.components.NpcString;

/**
 * Created by IntelliJ IDEA. User: Darvin Date: 24.01.12 Time: 16:33
 */
public class RequestCallToChangeClass extends L2GameClientPacket
{

	@Override
	protected void readImpl() throws Exception
	{
	}

	@Override
	protected void runImpl() throws Exception
	{
		if(!Config.AWAKING_FREE)
		{
			Player player = getClient().getActiveChar();
			if(player == null)
			{
				return;
			}
			if(player.getVarB("GermunkusUSM"))
			{
				return;
			}
			int _cId = 0;
			for(ClassId Cl : ClassId.VALUES)
			{
				if(Cl.level() == 5 && player.getClassId().childOf(Cl))
				{
					_cId = Cl.getId();
					break;
				}
			}

			if(player.isDead())
			{
				sendPacket(new ExShowScreenMessage(NpcString.YOU_CANNOT_TELEPORT_WHILE_YOU_ARE_DEAD, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false, ExShowScreenMessage.STRING_TYPE, -1, false), new ExCallToChangeClass(_cId, false));
				return;
			}
			if(player.isInParty())
			{
				sendPacket(new ExShowScreenMessage(NpcString.YOU_CANNOT_TELEPORT_IN_PARTY_STATUS, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false, ExShowScreenMessage.STRING_TYPE, -1, false), new ExCallToChangeClass(_cId, false));
				return;
			}
			player.processQuestEvent("_10338_SeizeYourDestiny", "MemoryOfDisaster", null);
			//AwakingManager.getInstance().onStartQuestAccept(player);
		}
		else
		{
			Player player = getClient().getActiveChar();

			if(player == null)
			{
				return;
			}

			if(player.getLevel() < 85)
			{
				return;
			}

			if(player.getClassId().level() < 3)
			{
				return;
			}

			// TODO: Когда будет готов квест - удалить это нах
			AwakingManager.getInstance().SendReqToAwaking(player);
		}
	}
}
