package l2next.gameserver.model.instances;

import l2next.commons.collections.GArray;
import l2next.commons.geometry.Circle;
import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.instancemanager.ReflectionManager;
import l2next.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.GameObjectTasks;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Territory;
import l2next.gameserver.model.World;
import l2next.gameserver.model.Zone;
import l2next.gameserver.network.serverpackets.MagicSkillUse;
import l2next.gameserver.taskmanager.EffectTaskManager;
import l2next.gameserver.templates.StatsSet;
import l2next.gameserver.templates.ZoneTemplate;
import l2next.gameserver.templates.npc.NpcTemplate;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Александр
 * Skype: <p>fearsanya</p>
 */
public class DeadsGateInstance extends NpcInstance
{

	private static final long serialVersionUID = -6492758830962788189L;
	private final Creature _owner;
	private final Skill _skill;
	private final Skill _skill2;
	private ScheduledFuture<?> _targetTask;
	private ScheduledFuture<?> _destroyTask;
	private Zone _zone = null;
	private GArray<Creature> targets = new GArray<Creature>();

	public DeadsGateInstance(int objectId, NpcTemplate template, Creature owner, Skill skill, Skill skill2)
	{
		super(objectId, template);
		_owner = owner;
		_skill = skill;
		_skill2 = skill2;

		setReflection(owner.getReflection());
		setLevel(owner.getLevel());
		setTitle(owner.getName());
	}

	private class OnZoneEnterLeaveListenerImpl implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature actor)
		{
			for(Creature target : _zone.getInsideCreatures())
			{
				if(target == _owner && _skill.isOffensive() && target.getPvpFlag() <= 0 && (_owner.getPlayer().getParty() == target.getPlayer().getParty()))
				{
					continue;
				}
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature actor)
		{
			if(actor.getEffectList().getEffectsBySkill(_skill) != null)
			{
				actor.getEffectList().stopEffect(_skill);
			}
		}
	}

	public Creature getOwner()
	{
		return _owner;
	}

	@Override
	protected void onSpawn()
	{
		super.onSpawn();

		Circle c = new Circle(getLoc(), _skill.isAoE() ? _skill.getSkillRadius() : 200);
		c.setZmax(World.MAP_MAX_Z);
		c.setZmin(World.MAP_MIN_Z);

		StatsSet set = new StatsSet();
		set.set("name", "DeadsGateZoneDum");
		set.set("type", Zone.ZoneType.dummy);
		set.set("territory", new Territory().add(c));

		_zone = new Zone(new ZoneTemplate(set));
		_zone.setReflection(ReflectionManager.DEFAULT);
		_zone.addListener(new DeadsGateInstance.OnZoneEnterLeaveListenerImpl());
		_zone.setActive(true);

		_destroyTask = ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(this), 120000L);
		_targetTask = EffectTaskManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if(!_skill.isAoE())
				{
					for(Creature target : _zone.getInsideCreatures())
					{
						if(_skill.checkTarget(_owner, target, null, false, false) == null)
						{
							targets.add(target);
							_skill.useSkill(DeadsGateInstance.this, targets);
						}
					}
				}
				else
				{
					for(Creature t : _zone.getInsideCreatures())
					{
						if(_skill.checkTarget(_owner, t, null, false, false) == null)
						{
							if(t == _owner && _skill.isOffensive() && t.getPvpFlag() <= 0 && (_owner.getPlayer().getParty() == t.getPlayer().getParty()))
							{
								continue;
							}
							targets.add(t);
						}
					}
					broadcastPacket(new MagicSkillUse(DeadsGateInstance.this, DeadsGateInstance.this, _skill.getId(), _skill.getLevel(), 0, 0));
					broadcastPacket(new MagicSkillUse(DeadsGateInstance.this, DeadsGateInstance.this, _skill2.getId(), _skill2.getLevel(), 0, 0));
					_skill.useSkill(DeadsGateInstance.this, targets);
					_skill2.useSkill(DeadsGateInstance.this, targets);
				}
			}
		}, 1000L, _skill.getReuseDelay() != 0 ? _skill.getReuseDelay() : 3000L);
	}

	@Override
	public void onDelete()
	{
		super.onDelete();

		if(_destroyTask != null)
		{
			_destroyTask.cancel(false);
		}
		_destroyTask = null;
		if(_targetTask != null)
		{
			_targetTask.cancel(false);
		}
		_targetTask = null;

		_zone.removeListener(new DeadsGateInstance.OnZoneEnterLeaveListenerImpl());

		_zone.setActive(false);
		_zone = null;
	}

	@Override
	public boolean isInvul()
	{
		return true;
	}
}
