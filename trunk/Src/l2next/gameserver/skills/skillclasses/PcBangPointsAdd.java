package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.templates.StatsSet;

public class PcBangPointsAdd extends Skill
{
	public PcBangPointsAdd(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		int points = (int) _power;

		for(Creature target : targets)
		{
			if(target.isPlayer())
			{
				Player player = target.getPlayer();
				player.addPcBangPoints(points, false);
			}
			getEffects(activeChar, target, getActivateRate() > 0, false);
		}

		if(isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}