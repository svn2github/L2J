package l2next.gameserver.network.clientpackets;

import l2next.gameserver.data.xml.holder.EnchantItemHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.PcInventory;
import l2next.gameserver.model.items.etcitems.AppearanceStone;
import l2next.gameserver.model.items.etcitems.AppearanceStone.ShapeTargetType;
import l2next.gameserver.model.items.etcitems.AppearanceStone.ShapeType;
import l2next.gameserver.network.serverpackets.ExPutShapeShiftingTargetItemResult;
import l2next.gameserver.network.serverpackets.ExShapeShiftingResult;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.templates.item.ExItemType;
import l2next.gameserver.templates.item.ItemTemplate.Grade;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author kick
 **/
public class RequestExTryToPutShapeShiftingTargetItem extends L2GameClientPacket
{
	private int _targetItemObjId;

	@Override
	protected void readImpl()
	{
		_targetItemObjId = readD();
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
		ItemInstance stone = player.getAppearanceStone();
		if(targetItem == null || stone == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		if(!targetItem.canBeAppearance())
		{
			player.sendPacket(ExPutShapeShiftingTargetItemResult.FAIL);
			return;
		}

		if(targetItem.getLocation() != ItemInstance.ItemLocation.INVENTORY && targetItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)
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

		if(appearanceStone.getType() != ShapeType.RESTORE && targetItem.getVisualId() > 0 || appearanceStone.getType() == ShapeType.RESTORE && targetItem.getVisualId() == 0)
		{
			player.sendPacket(ExPutShapeShiftingTargetItemResult.FAIL);
			return;
		}

		if(!targetItem.isOther() && targetItem.getTemplate().getItemGrade() == Grade.NONE)
		{
			player.sendPacket(SystemMsg.YOU_CANNOT_MODIFY_OR_RESTORE_NOGRADE_ITEMS);
			return;
		}

		Grade[] stoneGrades = appearanceStone.getGrades();
		if(stoneGrades != null && stoneGrades.length > 0)
		{
			if(!ArrayUtils.contains(stoneGrades, targetItem.getTemplate().getItemGrade()))
			{
				player.sendPacket(SystemMsg.ITEM_GRADES_DO_NOT_MATCH);
				return;
			}
		}

		ShapeTargetType[] targetTypes = appearanceStone.getTargetTypes();
		if(targetTypes == null || targetTypes.length == 0)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		if(!ArrayUtils.contains(targetTypes, ShapeTargetType.ALL))
		{
			if(targetItem.isWeapon())
			{
				if(!ArrayUtils.contains(targetTypes, ShapeTargetType.WEAPON))
				{
					player.sendPacket(SystemMsg.WEAPONS_ONLY);
					return;
				}
			}
			else if(targetItem.isArmor())
			{
				if(!ArrayUtils.contains(targetTypes, ShapeTargetType.ARMOR))
				{
					player.sendPacket(SystemMsg.ARMOR_ONLY);
					return;
				}
			}
			else
			{
				if(!ArrayUtils.contains(targetTypes, ShapeTargetType.ACCESSORY))
				{
					player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
					return;
				}
			}
		}

		ExItemType[] itemTypes = appearanceStone.getItemTypes();
		if(itemTypes != null && itemTypes.length > 0)
		{
			if(!ArrayUtils.contains(itemTypes, targetItem.getExItemType()))
			{
				player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
				return;
			}
		}

		// Запрет на обработку чужих вещей, баг может вылезти на серверных лагах
		if(targetItem.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		player.sendPacket(new ExPutShapeShiftingTargetItemResult(ExPutShapeShiftingTargetItemResult.SUCCESS_RESULT, appearanceStone.getCost()));
	}
}