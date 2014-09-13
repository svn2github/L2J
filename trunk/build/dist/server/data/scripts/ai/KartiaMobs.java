package ai;

import java.util.Calendar;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import l2next.commons.threading.RunnableImpl;
import l2next.commons.util.Rnd;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.data.xml.holder.NpcHolder;
import l2next.gameserver.instancemanager.WorldStatisticsManager;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Skill.SkillType;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.GuardInstance;
import l2next.gameserver.model.instances.MonsterInstance;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.skills.AbnormalEffect;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.PositionUtils;

import instances.Kartia;

public class KartiaMobs extends Fighter
{

	public KartiaMobs(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	public void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();

		Reflection r = actor.getReflection();	
		if (r != null)
		{
			if(r instanceof Kartia)
			{
				Kartia kInst = (Kartia) r;	
				if (kInst.monsterSet.get("ruler").intValue() == actor.getNpcId())
					Functions.npcSay(actor, NpcString.HOW_ITS_IMPOSSIBLE_RETURNING_TO_ABYSS_AGAIN);
			}	
		}
		super.onEvtDead(killer);
	}
	
	@Override
	protected void onEvtAttacked(final Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();

		Reflection r = actor.getReflection();	
		if (r != null)
			if(r instanceof Kartia)
			{
				final Kartia kInst = (Kartia) r;	
				if ((kInst.status == 1) && (((Integer)kInst.monsterSet.get("overseer")).intValue() == actor.getNpcId()) && (actor.getCurrentHp() / actor.getMaxHp() <= 0.4D) && (!actor.isMoving))
				{
					Functions.npcSay(actor, NpcString.YOU_VERY_STRONG_FOR_MORTAL_I_RETREAT);
					Location loc = kInst._isPartyInstance ? new Location(-120865, -13904, -11440) : new Location(-111297, -13904, -11440);
					DefaultAI ai = (DefaultAI) actor.getAI();
					ai.addTaskMove(Location.findPointToStay(loc, 250, actor.getGeoIndex()), true);

					ThreadPoolManager.getInstance().schedule(new RunnableImpl()
					{
						@Override
						public void runImpl()
						{
							actor.doDie(attacker);
							actor.deleteMe();
							kInst.openRaidDoor();
						}
					}
					, 10000L);
				}				
			}	
		super.onEvtAttacked(attacker, damage);
	}


	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		final NpcInstance actor = getActor();
		
		actor.setRunning();

		if (actor instanceof GuardInstance)
		{
			actor.setBusy(true);
			actor.setHaveRandomAnim(true);
		}
		else if ((actor instanceof MonsterInstance))
		{
			actor.setRandomWalk(false);

			Reflection r = actor.getReflection();	
			if (r != null)
				if(r instanceof Kartia)
				{
					Kartia kInst = (Kartia) r;	
					if ((kInst.monsterSet.get("captivated")).intValue() != actor.getNpcId())
						if (kInst.status == 0)
						{
							actor.setRunning();
							Location loc = kInst._isPartyInstance ? new Location(-120921 + Rnd.get(-150, 150), -10452 + Rnd.get(-150, 150), -11680) : new Location(-111353 + Rnd.get(-150, 150), -10452 + Rnd.get(-150, 150), -11680);
							DefaultAI ai = (DefaultAI) actor.getAI();
							ai.addTaskMove(Location.findPointToStay(loc, 250, actor.getGeoIndex()), true);	
						}
					if (((Integer)kInst.monsterSet.get("overseer")).intValue() == actor.getNpcId())
						if (kInst.status == 0)
							Functions.npcSay(actor, NpcString.INTRUDERS_CANNOT_LEAVE_ALIVE);
					/*else if (((Integer)kInst.monsterSet.get("ruler")).intValue() == actor.getNpcId())
					{
						actor.setIsInvul(true);
						actor.startAbnormalEffect(AbnormalEffect.FLESH_STONE);
						actor.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, kInst.ruler);
						actor.startParalyzed();
					} */
				}	
		} 
	}		
}
