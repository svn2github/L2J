package l2next.gameserver.network.clientpackets;

import l2next.gameserver.data.xml.holder.EnchantItemHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.PcInventory;
import l2next.gameserver.model.items.etcitems.AppearanceStone;
import l2next.gameserver.model.items.etcitems.AppearanceStone.ShapeType;
import l2next.gameserver.network.serverpackets.ExPutShapeShiftingExtractionItemResult;
import l2next.gameserver.network.serverpackets.ExShapeShiftingResult;
import l2next.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author kick
 **/
public class RequestExTryToPutShapeShiftingEnchantSupportItem extends L2GameClientPacket
{
	private int _targetItemObjId;
	private int _extracItemObjId;

	@Override
	protected void readImpl()
	{
		_targetItemObjId = readD();
		_extracItemObjId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		if(player.isActionsDisabled() || player.isInStoreMode() || player.isInTrade())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		PcInventory inventory = player.getInventory();
		ItemInstance targetItem = inventory.getItemByObjectId(_targetItemObjId);
		ItemInstance extracItem = inventory.getItemByObjectId(_extracItemObjId);
		ItemInstance stone = player.getAppearanceStone();
		if(targetItem == null || extracItem == null || stone == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		if(!extracItem.canBeAppearance())
		{
			player.sendPacket(ExPutShapeShiftingExtractionItemResult.FAIL);
			return;
		}

		if(extracItem.getLocation() != ItemInstance.ItemLocation.INVENTORY && extracItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		if((stone = inventory.getItemByObjectId(stone.getObjectId())) == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		AppearanceStone appearanceStone = EnchantItemHolder.getInstance().getAppearanceStone(stone.getItemId());
		if(appearanceStone == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		if(appearanceStone.getType() == ShapeType.RESTORE || appearanceStone.getType() == ShapeType.FIXED)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			return;
		}

		/*if(!extracItem.isOther() && extracItem.getGrade() == ItemGrade.NONE)
		{
			player.sendPacket(SystemMsg.ITEM_GRADES_DO_NOT_MATCH);
			return;
		}*/

		//if(!extracItem.isOther() && targetItem.getTemplate().getItemGrade().ordinal() <= extracItem.getTemplate().getItemGrade().ordinal())
		//{
		//	player.sendPacket(SystemMsg.YOU_CANNOT_EXTRACT_FROM_ITEMS_THAT_ARE_HIGHERGRADE_THAN_ITEMS_TO_BE_MODIFIED);
		//	return;
		//}

		if(extracItem.getVisualId() > 0)
		{
			player.sendPacket(SystemMsg.YOU_CANNOT_EXTRACT_FROM_A_MODIFIED_ITEM);
			return;
		}

		if(targetItem.getExItemType() != extracItem.getExItemType())
		{
			player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
			return;
		}

		// Запрет на обработку чужих вещей, баг может вылезти на серверных лагах
		if(extracItem.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		player.setAppearanceExtractItem(extracItem);
		//ItemFunctions.addItem(player
		player.sendPacket(ExPutShapeShiftingExtractionItemResult.SUCCESS);
	}
}