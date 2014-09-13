package npc.model;

import l2next.commons.collections.GArray;
import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.MonsterInstance;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.MagicSkillUse;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.Location;

public class MushroomInstance extends MonsterInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7041621728637386679L;
	private static final int FANTASY_MUSHROOM = 18864;
	private static final int FANTASY_MUSHROOM_SKILL = 6427;

	private static final int RAINBOW_FROG = 18866;
	private static final int RAINBOW_FROG_SKILL = 6429;

	private static final int STICKY_MUSHROOM = 18865;
	private static final int STICKY_MUSHROOM_SKILL = 6428;

	private static final int ENERGY_PLANT = 18868;
	private static final int ENERGY_PLANT_SKILL = 6430;

	private static final int ABYSS_WEED = 18867;

	public MushroomInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}

	@Override
	public void reduceCurrentHp(double i, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if(isDead())
		{
			return;
		}

		// ���� ���� ���� ���� ������, �� ������� ������ ���� �������.
		Creature killer = attacker;
		if(killer.isPet() || killer.isSummon())
		{
			killer = killer.getPlayer();
		}

		if(getNpcId() == RAINBOW_FROG) // ���� ��� ������� ������.
		{
			ThreadPoolManager.getInstance().schedule(new TaskAfterDead(this, killer, RAINBOW_FROG_SKILL), 3000);
			doDie(killer);
		}
		else if(getNpcId() == STICKY_MUSHROOM) // ���� ��� ����� � � ������ 40% ������ �����.
		{
			ThreadPoolManager.getInstance().schedule(new TaskAfterDead(this, killer, STICKY_MUSHROOM_SKILL), 3000);
			doDie(killer);
		}
		else if(getNpcId() == ENERGY_PLANT) // ���� ��� �����.
		{
			ThreadPoolManager.getInstance().schedule(new TaskAfterDead(this, killer, ENERGY_PLANT_SKILL), 3000);
			doDie(killer);
		}
		else if(getNpcId() == ABYSS_WEED)
		{
			doDie(killer);
		}
		else if(getNpcId() == FANTASY_MUSHROOM) // ���� ��� ������� ���� ����� � ���������� � ������ ��.
		{
			GArray<NpcInstance> around = getAroundNpc(700, 300);
			if(around != null && !around.isEmpty())
			{
				for(NpcInstance npc : around)
				{
					if(npc.isMonster() && npc.getNpcId() >= 22768 && npc.getNpcId() <= 22774)
					{
						npc.setRunning();
						npc.moveToLocation(Location.findPointToStay(this, 20, 50), 0, true);
					}
				}
			}
			ThreadPoolManager.getInstance().schedule(new TaskAfterDead(this, killer, FANTASY_MUSHROOM_SKILL), 4000);
		}
	}

	public static class TaskAfterDead extends RunnableImpl
	{
		private NpcInstance _actor;
		private Creature _killer;
		private Skill _skill;

		public TaskAfterDead(NpcInstance actor, Creature killer, int skillId)
		{
			_actor = actor;
			_killer = killer;
			_skill = SkillTable.getInstance().getInfo(skillId, 1);
		}

		@Override
		public void runImpl() throws Exception
		{
			if(_skill == null)
			{
				return;
			}

			if(_actor != null && _actor.getNpcId() == FANTASY_MUSHROOM)
			{
				_actor.broadcastPacket(new MagicSkillUse(_actor, _skill.getId(), _skill.getLevel(), 0, 0));
				GArray<NpcInstance> around = _actor.getAroundNpc(200, 300);
				if(around != null && !around.isEmpty())
				{
					for(NpcInstance npc : around)
					{
						if(npc.isMonster() && npc.getNpcId() >= 22768 && npc.getNpcId() <= 22774)
						{
							_skill.getEffects(npc, npc, false, false);
						}
					}
				}
				_actor.doDie(_killer);
				return;
			}

			if(_killer != null && _killer.isPlayer() && !_killer.isDead())
			{
				GArray<Creature> targets = new GArray<Creature>();
				targets.add(_killer);
				_killer.broadcastPacket(new MagicSkillUse(_killer, _killer, _skill.getId(), _skill.getLevel(), 0, 0));
				_skill.useSkill(_killer, targets);
			}
		}
	}
}