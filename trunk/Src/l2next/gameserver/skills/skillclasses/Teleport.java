package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.commons.util.Rnd;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.templates.StatsSet;
import l2next.gameserver.utils.Location;

public class Teleport extends Skill
{
	private static final Location[] TELEPORT_PLAYER_COORDS = {
		new Location(219368, 112616, -1330, 63),
		new Location(219176, 119368, -1760, 233),
		new Location(216920, 120264, -1760, 255),
		new Location(210488, 119800, -1345, 97),
		new Location(213688, 116424, -921, 33),
		new Location(215032, 114200, -921, 15),
	};

	public Teleport(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		for(Creature target : targets)
		{
			if(target != null && target.isPlayer())
			{
				Location coords = TELEPORT_PLAYER_COORDS[Rnd.get(TELEPORT_PLAYER_COORDS.length)];
				target.teleToLocation(coords);
			}
		}
	}
}