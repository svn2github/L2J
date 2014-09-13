package ai;

import l2next.commons.util.Rnd;
import l2next.gameserver.ai.Mystic;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.MonsterInstance;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.stats.Env;
import l2next.gameserver.stats.Stats;
import l2next.gameserver.stats.funcs.Func;
import l2next.gameserver.templates.npc.MinionData;

/**@Author Awakeninger
 * При спавне саммонят случайную охрану.
 * Защита прямо пропорциональна количеству охранников.
private static final int Mob1 = 23012;
private static final int Mob2 = 23010;
private static final int Mob3 = 23011;
 */
public class SquareMobs extends Mystic
{
	private static final int[] Servitors = {
		23012,
		23011,
		23010
	};

	private int _lastMinionCount = 12;

	private class FuncMulMinionCount extends Func
	{
		public FuncMulMinionCount(Stats stat, int order, Object owner)
		{
			super(stat, order, owner);
		}

		@Override
		public void calc(Env env)
		{
			env.value *= _lastMinionCount;
		}
	}

	public SquareMobs(NpcInstance actor)
	{
		super(actor);

		actor.addStatFunc(new FuncMulMinionCount(Stats.MAGIC_DEFENCE, 0x30, actor));
		actor.addStatFunc(new FuncMulMinionCount(Stats.POWER_DEFENCE, 0x30, actor));
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();

		NpcInstance actor = getActor();
		actor.getMinionList().addMinion(new MinionData(Servitors[Rnd.get(Servitors.length)], Rnd.get(2)));
		_lastMinionCount = Math.max(actor.getMinionList().getAliveMinions().size(), 1);
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		MonsterInstance actor = (MonsterInstance) getActor();
		if(actor.isDead())
		{
			return;
		}
		_lastMinionCount = Math.max(actor.getMinionList().getAliveMinions().size(), 1);
		super.onEvtAttacked(attacker, damage);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();
		actor.getMinionList().deleteMinions();
		super.onEvtDead(killer);
	}
}