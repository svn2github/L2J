package l2next.gameserver.network.clientpackets;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.data.xml.holder.EnchantItemHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.etcitems.LifeStone;
import l2next.gameserver.network.serverpackets.ExPutCommissionResultForVariationMake;

public class RequestConfirmGemStone extends AbstractRefinePacket
{
	// format: (ch)dddd
	private int _targetItemObjId;
	private int _refinerItemObjId;
	private int _gemstoneItemObjId;
	private long _gemstoneCount;

	@Override
	protected void readImpl()
	{
		_targetItemObjId = readD();
		_refinerItemObjId = readD();
		_gemstoneItemObjId = readD();
		_gemstoneCount = readQ();
	}

	@Override
	protected void runImpl()
	{
		if(_gemstoneCount <= 0)
		{
			return;
		}

		Player activeChar = getClient().getActiveChar();
		ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_targetItemObjId);
		ItemInstance refinerItem = activeChar.getInventory().getItemByObjectId(_refinerItemObjId);
		ItemInstance gemstoneItem = activeChar.getInventory().getItemByObjectId(_gemstoneItemObjId);

		if(targetItem == null || refinerItem == null || gemstoneItem == null)
		{
			activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}

		LifeStone lsi = EnchantItemHolder.getInstance().getLifeStone(refinerItem.getItemId());

		if(lsi == null)
		{
			return;
		}

		if(!isValid(activeChar, targetItem, refinerItem, gemstoneItem))
		{
			activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}

		if(_gemstoneCount != getGemStoneCount(lsi.getGrade(), targetItem.getTemplate().getItemGrade()))
		{
			activeChar.sendPacket(Msg.GEMSTONE_QUANTITY_IS_INCORRECT);
			return;
		}

		activeChar.sendPacket(new ExPutCommissionResultForVariationMake(_gemstoneItemObjId, _gemstoneCount), Msg.PRESS_THE_AUGMENT_BUTTON_TO_BEGIN);
	}
}