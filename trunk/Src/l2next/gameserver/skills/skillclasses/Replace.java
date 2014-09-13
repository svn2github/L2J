package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Summon;
import l2next.gameserver.templates.StatsSet;
import l2next.gameserver.utils.Location;

public class Replace extends Skill
{

	public Replace(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		if(!(activeChar instanceof Player))
		{
			return;
		}
		final Player activePlayer = activeChar.getPlayer();

		// TODO: Уточнить как долно работать...
		for(Summon activePet : activeChar.getPets())
		{
			if(!(activePet instanceof Summon))
			{
				return; // TODO: SysMessage
			}

			Location loc_pet = activePet.getLoc();
			Location loc_cha = activePlayer.getLoc();
			activePlayer.teleToLocation(loc_pet);
			activePet.teleToLocation(loc_cha);
		}
	}
}
