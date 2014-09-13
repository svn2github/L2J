/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ai;

import l2next.commons.util.Rnd;
import l2next.gameserver.Config;
import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.MonsterInstance;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.utils.Location;

import java.util.concurrent.ScheduledFuture;

public class Fairy extends Fighter
{

	private long _ReuseTimer = 0;

	ScheduledFuture<?> _followTask;
	Creature master = null;

	public Fairy(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 9000;
	}

	@Override
	public boolean canAttackCharacter(Creature target)
	{
		return target.isMonster();
	}

	@Override
	protected void onEvtThink()
	{
		final NpcInstance actor = getActor();
		if(master == null)
		{
			master = getActor().getFollowTarget();
		}
		if(master == null)
		{
			return;
		}
		if(master.isDead())
		{
			actor.deleteMe();
		}
		//Check for Aggression
		if(actor.getNpcId() == 32913 || actor.getNpcId() == 33098 || actor.getNpcId() == 33097)
		{
			if(master != null && !master.isDead() && master.getTarget() != null)
			{
				if(!actor.isCastingNow() && (_ReuseTimer < System.currentTimeMillis()))
				{
					for(NpcInstance npc : actor.getAroundNpc(600, 100))
					{
						if(npc instanceof MonsterInstance)
						{
							if(npc.getTarget().isPlayer())
							{
								actor.doCast(SkillTable.getInstance().getInfo(10060, 1), npc, true);
								_ReuseTimer = System.currentTimeMillis() + (7 * 1000L);
							}
						}
					}
				}
			}
		}
		if(getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			//Check for Mobs to Attack
			int mobscount = 0;
			for(NpcInstance npc : actor.getAroundNpc(600, 100))
			{
				if(npc instanceof MonsterInstance)
				{
					actor.getAggroList().addDamageHate(npc, 10, 10);
					mobscount++;
				}
			}
			if(mobscount > 0 && !actor.getAggroList().isEmpty())
			{
				Attack(actor.getAggroList().getRandomHated(), true, false);
			}
			//Check for Follow
			else
			{
				if(getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
				{
					setIntention(CtrlIntention.AI_INTENTION_FOLLOW);
				}
				if(master != null && actor.getDistance(master.getLoc()) > 300)
				{
					final Location loc = new Location(master.getX() + Rnd.get(-120, 120), master.getY() + Rnd.get(-120, 120), master.getZ());
					actor.followToCharacter(loc, master, Config.FOLLOW_RANGE, false);
					actor.setRunning();
				}
			}
		}
		super.onEvtThink();
	}
}
