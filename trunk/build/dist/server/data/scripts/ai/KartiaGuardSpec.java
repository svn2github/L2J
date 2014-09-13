package ai;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import l2next.commons.threading.RunnableImpl;
import l2next.commons.util.Rnd;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.ai.Guard;
import l2next.gameserver.data.xml.holder.NpcHolder;
import l2next.gameserver.instancemanager.WorldStatisticsManager;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Skill.SkillType;
import l2next.gameserver.model.instances.GuardInstance;
import l2next.gameserver.model.instances.MonsterInstance;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.skills.AbnormalEffect;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.PositionUtils;

import instances.Kartia;

public class KartiaGuardSpec extends Guard
{

	public KartiaGuardSpec(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		if(getActor().getNpcId() == 33641 || getActor().getNpcId() == 33643 || getActor().getNpcId() == 33645)
			if(target.isPlayer())
				return;
		super.onEvtAggression(target, aggro);
	}


	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		final NpcInstance actor = getActor();
		
		actor.setRunning();

		actor.setBusy(true);
		actor.setHaveRandomAnim(false);
	}		
}
