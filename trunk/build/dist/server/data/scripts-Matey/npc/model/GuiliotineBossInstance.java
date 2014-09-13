package npc.model;

import l2next.gameserver.listener.actor.OnCurrentHpDamageListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.instances.RaidBossInstance;
import l2next.gameserver.templates.npc.NpcTemplate;

public class GuiliotineBossInstance extends RaidBossInstance
{
	
	private int _mob;
	private CurrentHpListener _currentHpListener = new CurrentHpListener();
	private static final long serialVersionUID = -2947688161593683766L;

	public GuiliotineBossInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setState(0);
	}
	
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if(actor.getNpcId() == 25885)
			{
				if(actor == null || actor.isDead())
				{
					return;
				}

				if(actor.getCurrentHp() <= 2750000 && getState() == 1)
				{
					NpcInstance Guard1 = getReflection().addSpawnWithoutRespawn(25889, getLoc(), 0);
					setState(2);
				}
				
				if(actor.getCurrentHp() <= 1800000 && getState() == 2)
				{
					NpcInstance Guard2 = getReflection().addSpawnWithoutRespawn(25890, getLoc(), 0);
					setState(3);
				}
				
				if(actor.getCurrentHp() <= 900000 && getState() == 3)
				{
					NpcInstance Guard3 = getReflection().addSpawnWithoutRespawn(25891, getLoc(), 0);
					setState(4);
				}
				
				if(actor.getCurrentHp() <= 100000 && getState() == 4)
				{
					decayMe();
					doDie(actor);
					NpcInstance Gui3 = getReflection().addSpawnWithoutRespawn(25892, getLoc(), 0);
					setState(5);
				}
				//TODO case 25892 [A]: Сделать спаун рабов с аи агров на пати. 25893
			}
		}

	}

	@Override
	protected void onReduceCurrentHp(double damage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp)
	{
		switch(getNpcId())
		{
			case 25888:
			{
				if(this.getCurrentHp() <= 5000  && getState() == 0)
				{
					this.decayMe();
					NpcInstance Gui2 = getReflection().addSpawnWithoutRespawn(25885, getLoc(), 0);
					Gui2.addListener(_currentHpListener);
					setState(1);
				}
				break;
			}	
		}
		super.onReduceCurrentHp(damage, attacker, skill, awake, standUp, directHp);
	}
	
	private void setState(int mob)
	{
		_mob = mob;
	}
	
	private int getState()
	{
		return _mob;
	}
}