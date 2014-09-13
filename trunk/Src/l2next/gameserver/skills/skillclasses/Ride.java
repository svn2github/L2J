package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.templates.StatsSet;

public class Ride extends Skill
{
	public Ride(StatsSet set)
	{
		super(set);
	}

	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if(!activeChar.isPlayer())
		{
			return false;
		}

		Player player = (Player) activeChar;
		if(getNpcId() != 0)
		{
			if(player.isInOlympiadMode())
			{
				player.sendPacket(Msg.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
				return false;
			}
			if(player.isInDuel() || player.isSitting() || player.isInCombat() || player.isFishing() || player.isCursedWeaponEquipped() || player.getTransformation() != 0 || player.getPetCount() > 0 || player.isMounted() || player.isInBoat())
			{
				player.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
				return false;
			}
		}
		else if(getNpcId() == 0 && !player.isMounted())
		{
			return false;
		}

		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature caster, GArray<Creature> targets)
	{
		if(!caster.isPlayer())
		{
			return;
		}

		Player activeChar = (Player) caster;
		activeChar.setMount(getNpcId(), 0, 0);
	}
}