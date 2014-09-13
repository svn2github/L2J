package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.Config;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.templates.StatsSet;

public class VitalityHeal extends Skill
{
	public VitalityHeal(StatsSet set)
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

		Player p = target.getPlayer();

		if(p.getVitality().getItems() == 0)
		{
			return false;
		}

		p.getVitality().decItems();

		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		int fullPoints = Config.MAX_VITALITY;
		double percent = _power;

		for(Creature target : targets)
		{
			if(target.isPlayer())
			{
				Player player = target.getPlayer();
				int points = (int) (fullPoints / 100 * percent);
				player.getVitality().incPoints(points);
			}
			getEffects(activeChar, target, getActivateRate() > 0, false);
		}

		if(isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}