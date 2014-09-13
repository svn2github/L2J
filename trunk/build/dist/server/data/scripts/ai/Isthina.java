package ai;

import l2next.commons.threading.RunnableImpl;
import l2next.commons.util.Rnd;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Zone;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.EventTrigger;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.PlaySound;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.utils.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Описать и сделать скилы
 */
public class Isthina extends DefaultAI
{
	//NPC ID
	final int ISTINA_LIGHT = 29195;
	final int ISTINA_HARD = 29196;
	//SKILLS
	private static final Skill BARRIER_OF_REFLECTION = SkillTable.getInstance().getInfo(14215, 1);
	private static final Skill FLOOD = SkillTable.getInstance().getInfo(14220, 1);
	private static final Skill MANIFESTATION_OF_AUTHORITY = SkillTable.getInstance().getInfo(14289, 1);
	private static final Skill ACID_ERUPTION1 = SkillTable.getInstance().getInfo(14221, 1);
	private static final Skill ACID_ERUPTION2 = SkillTable.getInstance().getInfo(14222, 1);
	private static final Skill ACID_ERUPTION3 = SkillTable.getInstance().getInfo(14223, 1);
	private static final Skill DEATH_BLOW = SkillTable.getInstance().getInfo(14219, 1);
	private static final Skill ISTHINA_MARK = SkillTable.getInstance().getInfo(14218, 1);
	//RING zone (Trigger)
	final int RED_RING = 14220101;
	final int BLUE_RING = 14220102;
	final int GREEN_RING = 14220103;
	//RING LOCATIONS
	final Zone RED_RING_LOC;
	final Zone BLUE_RING_LOC;
	final Zone GREEN_RING_LOC;

	private boolean _authorityLock = false;

	private int _ring;

	private static Zone _zone;

	public Isthina(NpcInstance actor)
	{
		super(actor);
		_zone = ReflectionUtils.getZone("[Isthina_epic]");
		RED_RING_LOC = ReflectionUtils.getZone("[Isthina_red_zone]");
		BLUE_RING_LOC = ReflectionUtils.getZone("[Isthina_blue_zone]");
		GREEN_RING_LOC = ReflectionUtils.getZone("[Isthina_green_zone]");
	}

	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		Creature target;
		if((target = prepareTarget()) == null)
		{
			return false;
		}

		NpcInstance npc = getActor();

		if(npc.isDead())
		{
			return false;
		}

		double distance = npc.getDistance(target);

		//ThreadPoolManager.getInstance().scheduleAtFixedRate(new EffectCheckTask(npc), 0, 2000);

		double damage = npc.getMaxHp() - npc.getCurrentHp();

		double lastPercentHp = (npc.getCurrentHp() + damage) / npc.getMaxHp();
		double currentPercentHp = npc.getCurrentHp() / npc.getMaxHp();

		if(Rnd.chance(10) && lastPercentHp > 0.9D && currentPercentHp <= 0.9D)
		{
			onPercentHpReached(npc, 90);
		}
		/*
		else if((lastPercentHp > 0.8D) && (currentPercentHp <= 0.8D))
			onPercentHpReached(npc, 80);
		else if((lastPercentHp > 0.7D) && (currentPercentHp <= 0.7D))
			onPercentHpReached(npc, 70);
		else if((lastPercentHp > 0.6D) && (currentPercentHp <= 0.6D))
			onPercentHpReached(npc, 60);
		else if((lastPercentHp > 0.5D) && (currentPercentHp <= 0.5D))
			onPercentHpReached(npc, 50);
		else if((lastPercentHp > 0.45D) && (currentPercentHp <= 0.45D))
			onPercentHpReached(npc, 45);
		else if((lastPercentHp > 0.4D) && (currentPercentHp <= 0.4D))
			onPercentHpReached(npc, 40);
		else if((lastPercentHp > 0.35D) && (currentPercentHp <= 0.35D))
			onPercentHpReached(npc, 35);
		else if((lastPercentHp > 0.3D) && (currentPercentHp <= 0.3D))
			onPercentHpReached(npc, 30);
		else if((lastPercentHp > 0.25D) && (currentPercentHp <= 0.25D))
			onPercentHpReached(npc, 25);
		else if((lastPercentHp > 0.2D) && (currentPercentHp <= 0.2D))
			onPercentHpReached(npc, 20);
		else if((lastPercentHp > 0.15D) && (currentPercentHp <= 0.15D))
			onPercentHpReached(npc, 15);
		else if((lastPercentHp > 0.1D) && (currentPercentHp <= 0.1D))
			onPercentHpReached(npc, 10);
		else if((lastPercentHp > 0.05D) && (currentPercentHp <= 0.05D))
			onPercentHpReached(npc, 5);
		else if((lastPercentHp > 0.02D) && (currentPercentHp <= 0.02D))
			onPercentHpReached(npc, 2);
		else if((lastPercentHp > 0.01D) && (currentPercentHp <= 0.01D))
			onPercentHpReached(npc, 1);
			*/

		if(Rnd.chance(5) && !_authorityLock)
		{
			authorityField(npc);
		}

		if(Rnd.chance(30))
		{
			Map<Skill, Integer> d_skill = new HashMap<Skill, Integer>();

			addDesiredSkill(d_skill, target, distance, ACID_ERUPTION1);
			addDesiredSkill(d_skill, target, distance, ACID_ERUPTION2);
			addDesiredSkill(d_skill, target, distance, ACID_ERUPTION3);
			addDesiredSkill(d_skill, target, distance, ISTHINA_MARK);
			addDesiredSkill(d_skill, target, distance, DEATH_BLOW);

			Skill r_skill = selectTopSkill(d_skill);
			if(r_skill != null && !r_skill.isOffensive())
			{
				target = npc;
			}

			return chooseTaskAndTargets(r_skill, target, distance);
		}

		return chooseTaskAndTargets(null, target, distance);

	}

	private void authorityField(NpcInstance npc)
	{
		_authorityLock = true;

		double seed = Rnd.get();

		final int ring = seed >= 0.33D && seed < 0.66D ? 1 : seed < 0.33D ? 0 : 2;
		_ring = ring;
		if(seed < 0.33D)
		{
			npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_GREEN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
		}
		else if(seed >= 0.33D && seed < 0.66D)
		{
			npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_BLUE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
		}
		else
		{
			npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_RED, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
		}
		npc.broadcastPacket(new PlaySound("istina.istina_voice_01"));

		ThreadPoolManager.getInstance().schedule(new runAuthorityRing(npc), 10000L);
	}

	public void onPercentHpReached(NpcInstance npc, int percent)
	{
		if(npc.getEffectList().getEffectsBySkill(FLOOD) == null && percent >= 50 && percent % 10 == 0 || percent < 50 && percent % 5 == 0)
		{
			npc.doCast(FLOOD, npc, false);
			npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_GETS_FURIOUS_AND_RECKLESSLY_CRAZY, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));

		}
		if(npc.getEffectList().getEffectsBySkill(BARRIER_OF_REFLECTION) == null)
		{
			npc.doCast(BARRIER_OF_REFLECTION, npc, false);
			npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SPREADS_PROTECTIVE_SHEET, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
		}
	}

	/*
	private class EffectCheckTask extends RunnableImpl
	{
		private NpcInstance _npc;

		public EffectCheckTask(NpcInstance npc)
		{
			_npc = npc;
		}
		
		@Override
		public void runImpl()
		{
			if(_npc == null)
			{
				return;
			}

			boolean hasBarrier = false;
			boolean hasFlood = false;
			if(_npc.getEffectList().getEffectsBySkill(BARRIER_OF_REFLECTION) != null)
			{
				hasBarrier = true;
				if(hasFlood)
					return;
			}
			else
			{
				if(_npc.getEffectList().getEffectsBySkill(FLOOD) != null)
				hasFlood = true;
				if(hasBarrier)
					return;
			}
			if((_hasBarrier) && (!hasBarrier))
			{
				_npc.setNpcState(2);
				_npc.setNpcState(0);
				_npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SPREADS_PROTECTIVE_SHEET, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			}
			else if((!_hasBarrier) && (hasBarrier))
				_npc.setNpcState(1);

			if((_hasFlood) && (hasFlood))
				_npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_GETS_FURIOUS_AND_RECKLESSLY_CRAZY, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			else if((_hasFlood) && (!hasFlood))
				_npc.broadcastPacket(new ExShowScreenMessage(NpcString.BERSERKER_OF_ISTINA_HAS_BEEN_DISABLED, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
		}
	}
	*/

	private class runAuthorityRing extends RunnableImpl
	{
		private NpcInstance _npc;

		runAuthorityRing(NpcInstance npc)
		{
			_npc = npc;
		}

		@Override
		public void runImpl()
		{
			NpcInstance npc = _npc;

			_authorityLock = false;

			Zone zones;

			if(_ring == 2)
			{
				npc.broadcastPacket(new EventTrigger(RED_RING, false));
				npc.broadcastPacket(new EventTrigger(GREEN_RING, false));
				npc.broadcastPacket(new EventTrigger(BLUE_RING, false));
				zones = RED_RING_LOC;
			}
			else if(_ring == 0)
			{
				npc.broadcastPacket(new EventTrigger(BLUE_RING, false));
				npc.broadcastPacket(new EventTrigger(GREEN_RING, false));
				npc.broadcastPacket(new EventTrigger(RED_RING, false));
				zones = GREEN_RING_LOC;
			}
			else
			{
				npc.broadcastPacket(new EventTrigger(RED_RING, false));
				npc.broadcastPacket(new EventTrigger(BLUE_RING, false));
				npc.broadcastPacket(new EventTrigger(GREEN_RING, false));
				zones = BLUE_RING_LOC;
			}
			for(Player player : _zone.getInsidePlayers())
			{
				if(!player.isInZone(zones))
				{
					MANIFESTATION_OF_AUTHORITY.getEffects(npc, player, false, false);
					zones.doLeave(player);
				}
			}
		}
	}
}