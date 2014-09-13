package l2next.gameserver.network.clientpackets;

import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Summon;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.utils.ItemFunctions;

public class RequestPetGetItem extends L2GameClientPacket
{
	// format: cd
	private int _objectId;

	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		if(activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}

		Summon summon = activeChar.getFirstPet();
		if(summon == null || !summon.isPet() || summon.isDead() || summon.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}

		ItemInstance item = (ItemInstance) activeChar.getVisibleObject(_objectId);
		if(item == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if(!ItemFunctions.checkIfCanPickup(summon, item))
		{
			SystemMessage sm;
			if(item.getItemId() == 57)
			{
				sm = new SystemMessage(SystemMessage.YOU_HAVE_FAILED_TO_PICK_UP_S1_ADENA);
				sm.addNumber(item.getCount());
			}
			else
			{
				sm = new SystemMessage(SystemMessage.YOU_HAVE_FAILED_TO_PICK_UP_S1);
				sm.addItemName(item.getItemId());
			}
			sendPacket(sm);
			activeChar.sendActionFailed();
			return;
		}

		summon.getAI().setIntention(CtrlIntention.AI_INTENTION_PICK_UP, item, null);
	}
}