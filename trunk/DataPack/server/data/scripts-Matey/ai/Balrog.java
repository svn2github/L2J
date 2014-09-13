package ai;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2next.commons.util.Rnd;
import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.party.Party;
import l2next.gameserver.network.serverpackets.MagicSkillUse;
import l2next.gameserver.scripts.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Awakeninger
 */
public class Balrog extends DefaultAI
{
	final Skill Invincible;
	final Skill Freezing;

	private static final int Water_Dragon_Scale = 2369;
	private static final int Tears_Copy = 25535;

	ScheduledFuture<?> spawnTask;
	ScheduledFuture<?> despawnTask;

	List<NpcInstance> spawns = new ArrayList<NpcInstance>();

	private boolean _isUsedInvincible = false;

	private int _scale_count = 0;
	private long _last_scale_time = 0;

	public Balrog(NpcInstance actor)
	{
		super(actor);

		TIntObjectHashMap<Skill> skills = getActor().getTemplate().getSkills();

		Invincible = skills.get(5420);
		Freezing = skills.get(5238);
	}

	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		NpcInstance actor = getActor();
		if(actor.isDead() || skill == null || caster == null)
		{
			return;
		}

		if(System.currentTimeMillis() - _last_scale_time > 5000)
		{
			_scale_count = 0;
		}

		if(skill.getId() == Water_Dragon_Scale)
		{
			_scale_count++;
			_last_scale_time = System.currentTimeMillis();
		}

		Player player = caster.getPlayer();
		if(player == null)
		{
			return;
		}

		int count = 1;
		Party party = player.getParty();
		if(party != null)
		{
			count = party.getMemberCount();
		}

		// Снимаем неуязвимость
		if(_scale_count >= count)
		{
			_scale_count = 0;
			actor.getEffectList().stopEffect(Invincible);
		}
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

		NpcInstance actor = getActor();
		if(actor.isDead())
		{
			return false;
		}

		double distance = actor.getDistance(target);
		double actor_hp_precent = actor.getCurrentHpPercents();
		int rnd_per = Rnd.get(100);

		if(actor_hp_precent < 15 && !_isUsedInvincible)
		{
			_isUsedInvincible = true;
			addTaskBuff(actor, Invincible);
			Functions.npcSay(actor, "Готовьтесь к смерти!!!");
			return true;
		}

		if(rnd_per < 5 && spawnTask == null && despawnTask == null)
		{
			actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 5441, 1, 3000, 0));
			//spawnTask = ThreadPoolManager.getInstance().schedule(new SpawnMobsTask(), 3000);
			return true;
		}

		if(!actor.isAMuted() && rnd_per < 75)
		{
			return chooseTaskAndTargets(null, target, distance);
		}

		return chooseTaskAndTargets(Freezing, target, distance);
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}