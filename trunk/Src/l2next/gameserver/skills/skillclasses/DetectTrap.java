package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.World;
import l2next.gameserver.model.instances.TrapInstance;
import l2next.gameserver.network.serverpackets.NpcInfo;
import l2next.gameserver.templates.StatsSet;

public class DetectTrap extends Skill
{
	public DetectTrap(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		for(Creature target : targets)
		{
			if(target != null && target.isTrap())
			{
				TrapInstance trap = (TrapInstance) target;
				if(trap.getLevel() <= getPower())
				{
					trap.setDetected(true);
					for(Player player : World.getAroundPlayers(trap))
					{
						player.sendPacket(new NpcInfo(trap, player));
					}
				}
			}
		}

		if(isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}