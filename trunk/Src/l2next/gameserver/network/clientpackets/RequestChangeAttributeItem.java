package l2next.gameserver.network.clientpackets;

import l2next.commons.dao.JdbcEntityState;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.Element;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.PcInventory;
import l2next.gameserver.network.serverpackets.ExChangeAttributeFail;
import l2next.gameserver.network.serverpackets.ExChangeAttributeOk;
import l2next.gameserver.network.serverpackets.InventoryUpdate;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.SystemMsg;

public class RequestChangeAttributeItem extends L2GameClientPacket
{
	private int _consumeItemId, _itemObjId, _newElementId;

	@Override
	public void readImpl()
	{
		_consumeItemId = readD();
		_itemObjId = readD();
		_newElementId = readD();
	}

	@Override
	public void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		PcInventory inventory = activeChar.getInventory();
		ItemInstance _item = inventory.getItemByObjectId(_itemObjId);
		ItemInstance item_stone = inventory.getItemByObjectId(_consumeItemId);
		int privateStore = activeChar.getPrivateStoreType();

		Element oldElement = _item.getAttackElement();
		Element newElement = Element.VALUES[_newElementId];

		if(activeChar.getEnchantScroll() != null)
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.ENCHANTMENT_OR_ATTRIBUTE_ENCHANTMENT_IS_IN_PROGRESS));
			activeChar.sendPacket(new SystemMessage2(SystemMsg.CHANGING_ATTRIBUTES_HAS_BEEN_FAILED));
			activeChar.sendPacket(new ExChangeAttributeFail());
			return;
		}

		if(activeChar.isInStoreMode() || privateStore == 0 || privateStore == 1 || privateStore == 3 || privateStore == 5)
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_CANNOT_CHANGE_AN_ATTRIBUTE_WHILE_USING_A_PRIVATE_SHOP_OR_WORKSHOP));
			activeChar.sendPacket(new SystemMessage2(SystemMsg.CHANGING_ATTRIBUTES_HAS_BEEN_FAILED));
			activeChar.sendPacket(new ExChangeAttributeFail());
			return;
		}

		if(activeChar.isProcessingRequest() || activeChar.isBusy())
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_CANNOT_CHANGE_ATTRIBUTES_WHILE_EXCHANGING));
			activeChar.sendPacket(new SystemMessage2(SystemMsg.CHANGING_ATTRIBUTES_HAS_BEEN_FAILED));
			activeChar.sendPacket(new ExChangeAttributeFail());
			return;
		}

		if(_item == null || item_stone == null)
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS));
			activeChar.sendPacket(new SystemMessage2(SystemMsg.CHANGING_ATTRIBUTES_HAS_BEEN_FAILED));
			activeChar.sendPacket(new ExChangeAttributeFail());
			return;
		}

		if(oldElement == newElement)
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.CHANGING_ATTRIBUTES_HAS_BEEN_FAILED));
			activeChar.sendPacket(new ExChangeAttributeFail());
		}

		if(_item.isEquipped())
		{
			activeChar.getInventory().isRefresh = true;
			activeChar.getInventory().unEquipItem(_item);
		}

		int elementVal = _item.getAttributeElementValue(oldElement, false);

		inventory.destroyItem(item_stone, 1);

		_item.setAttributeElement(oldElement, 0);

		_item.setAttributeElement(newElement, _item.getAttributeElementValue(newElement, false) + elementVal);
		_item.setJdbcState(JdbcEntityState.UPDATED);
		_item.update();

		if(_item.isEquipped())
		{
			activeChar.getInventory().equipItem(_item);
			activeChar.getInventory().isRefresh = false;
		}

		activeChar.sendPacket(new ExChangeAttributeOk());
		activeChar.sendPacket(new InventoryUpdate().addModifiedItem(_item));
	}
}