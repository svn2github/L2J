package l2next.gameserver.network.clientpackets;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.network.serverpackets.ExPutItemResultForVariationMake;

public class RequestConfirmTargetItem extends AbstractRefinePacket
{
	// format: (ch)d
	private int _itemObjId;

	@Override
	protected void readImpl()
	{
		_itemObjId = readD(); // object_id шмотки
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemObjId);

		if(item == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		if(!isValid(activeChar, item))
		{
			if(item.isAugmented())
			{
				activeChar.sendPacket(Msg.ONCE_AN_ITEM_IS_AUGMENTED_IT_CANNOT_BE_AUGMENTED_AGAIN);
				return;
			}

			activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}
		activeChar.sendPacket(new ExPutItemResultForVariationMake(_itemObjId), Msg.SELECT_THE_CATALYST_FOR_AUGMENTATION);
	}
}