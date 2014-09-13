package l2next.gameserver.network.clientpackets;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.network.serverpackets.ExGetCrystalizingEstimation;

/**
 * @author ALF
 * @modified KilRoy
 */
public class RequestCrystallizeEstimate extends L2GameClientPacket
{

	private int _objectId;
	private long _count;

	@Override
	protected void readImpl() throws Exception
	{
		_objectId = readD();
		_count = readQ();
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		if(player.isActionsDisabled())
		{
			player.sendActionFailed();
			return;
		}

		if(player.isInStoreMode())
		{
			player.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}

		if(player.isInTrade())
		{
			player.sendActionFailed();
			return;
		}

		ItemInstance item = player.getInventory().getItemByObjectId(_objectId);
		if(item == null)
		{
			player.sendActionFailed();
			return;
		}

		if(item.isHeroWeapon())
		{
			player.sendPacket(Msg.HERO_WEAPONS_CANNOT_BE_DESTROYED);
			return;
		}

		if(!item.canBeCrystallized(player))
		{
			player.sendActionFailed();
			return;
		}

		if(player.isInStoreMode())
		{
			player.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}

		if(player.isFishing())
		{
			player.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}

		if(player.isInTrade())
		{
			player.sendActionFailed();
			return;
		}

		if(item.getCount() < _count)
		{
			player.sendPacket(Msg.YOU_CANNOT_DESTROY_IT_BECAUSE_THE_NUMBER_IS_INCORRECT);
			return;
		}

		int crystalCount = item.getTemplate().getCrystalCount();
		int crystalId = item.getTemplate().getCrystalType().cry;
		int level = player.getSkillLevel(Skill.SKILL_CRYSTALLIZE);

		if(level < 1 || (item.getTemplate().getCrystalType().externalOrdinal > level))
		{
			player.sendPacket(Msg.CANNOT_CRYSTALLIZE_CRYSTALLIZATION_SKILL_LEVEL_TOO_LOW);
			player.sendActionFailed();
			return;
		}

		ExGetCrystalizingEstimation CE = new ExGetCrystalizingEstimation(crystalId, crystalCount, item.getTemplate().getExCrystallizationList());
		player.sendPacket(CE);
	}

}
