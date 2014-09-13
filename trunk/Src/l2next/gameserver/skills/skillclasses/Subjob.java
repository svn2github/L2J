package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.base.TeamType;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.templates.StatsSet;

/**
 * @author ALF
 * @data 25.02.2012
 */
public class Subjob extends Skill
{

	public Subjob(StatsSet set)
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

		Player p = activeChar.getPlayer();

		if(p.isInDuel() || p.getTeam() != TeamType.NONE)
		{
			activeChar.sendPacket(SystemMsg.THIS_TERRITORY_CAN_NOT_CHANGE_CLASS);
			return false;
		}

		if(p.isInOlympiadMode())
		{
			activeChar.sendPacket(SystemMsg.THIS_TERRITORY_CAN_NOT_CHANGE_CLASS);
			return false;
		}

		if(activeChar.getTransformation() > 0)
		{
			activeChar.sendPacket(SystemMsg.YOU_CAN_NOT_CHANGE_CLASS_IN_TRANSFORMATION);
			return false;
		}

		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		if(!activeChar.isPlayer())
		{
			return;
		}

		Player player = activeChar.getPlayer();

		switch(getId())
		{
			case 1566: // Main класс
			{
				player.changeClass(1);
				break;
			}
			case 1567: // Dual Class
			{
				player.changeClass(2);
				break;
			}
			case 1568: // Sub Class
				player.changeClass(3);
				break;
			case 1569: // Unk
				player.changeClass(4);
				break;
		}
	}

}
