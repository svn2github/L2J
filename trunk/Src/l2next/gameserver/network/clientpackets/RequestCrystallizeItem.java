package l2next.gameserver.network.clientpackets;

import l2next.commons.util.Rnd;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.items.CrystallizationItem;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.utils.ItemFunctions;
import l2next.gameserver.utils.Log;

public class RequestCrystallizeItem extends L2GameClientPacket
{
	private int _objectId;

	/* private long _count; */

	@Override
	protected void readImpl()
	{
		_objectId = readD();
		/* _count = */
		readQ();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();

		if(activeChar == null)
		{
			return;
		}

		if(activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isInStoreMode())
		{
			activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}

		if(activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}

		ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		if(item == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if(item.isHeroWeapon())
		{
			activeChar.sendPacket(Msg.HERO_WEAPONS_CANNOT_BE_DESTROYED);
			return;
		}

		if(!item.canBeCrystallized(activeChar))
		{
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isInStoreMode())
		{
			activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}

		if(activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}

		if(activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}

		int crystalAmount = item.getTemplate().getCrystalCount();
		int crystalId = item.getTemplate().getCrystalType().cry;
		int level = activeChar.getSkillLevel(Skill.SKILL_CRYSTALLIZE);

		if(level < 1 || (item.getTemplate().getCrystalType().externalOrdinal > level))
		{
			activeChar.sendPacket(Msg.CANNOT_CRYSTALLIZE_CRYSTALLIZATION_SKILL_LEVEL_TOO_LOW);
			System.out.println("Level: " + level);
			activeChar.sendActionFailed();
			return;
		}

		Log.LogItem(activeChar, Log.Crystalize, item);

		if(!activeChar.getInventory().destroyItemByObjectId(_objectId, 1L))
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendPacket(Msg.THE_ITEM_HAS_BEEN_SUCCESSFULLY_CRYSTALLIZED);
		ItemFunctions.addItem(activeChar, crystalId, crystalAmount, true);

		if(!item.getTemplate().getExCrystallizationList().isEmpty())
		{
			for(CrystallizationItem items : item.getTemplate().getExCrystallizationList())
			{
				if(Rnd.chance(items.getChance()))
				{
					ItemFunctions.addItem(activeChar, items.getItemId(), items.getCount(), true);
				}
			}
		}

		activeChar.sendChanges();
	}
}