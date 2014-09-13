package npc.model;

import l2next.commons.util.Rnd;
import l2next.gameserver.instancemanager.SoAManager;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.MonsterInstance;
import l2next.gameserver.templates.npc.MinionData;
import l2next.gameserver.templates.npc.NpcTemplate;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Минионы умирают, если умирает пушка.
 *
 * @author Awakeninger
 */
public class CrystallHallCannonInstance extends MonsterInstance
{
	private static final int[] CANNON_CUSTOM = new int[]{
		19008
	};
	private static final int[] CANNON_DOOR = new int[]{
		19009
	};
	private static final int[][] MINIONS = new int[][]{
		{
			23012,
			23012,
			23012
		},
		{
			23011,
			23011,
			23011
		},
		{
			23010,
			23010,
			23010
		}
	};

	public CrystallHallCannonInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		if(ArrayUtils.contains(CANNON_CUSTOM, template.getNpcId()))
		{
			addMinions(MINIONS[Rnd.get(MINIONS.length)], template);
		}
		else if(ArrayUtils.contains(CANNON_DOOR, template.getNpcId()))
		{
			addMinions(MINIONS[Rnd.get(MINIONS.length)], template);
		}
	}

	private static void addMinions(int[] minions, NpcTemplate template)
	{
		if(minions != null && minions.length > 0)
		{
			for(int id : minions)
			{
				template.addMinion(new MinionData(id, 3));
			}
		}
	}

	@Override
	protected void onDeath(Creature killer)
	{
		getMinionList().unspawnMinions();
		super.onDeath(killer);
		
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}
}