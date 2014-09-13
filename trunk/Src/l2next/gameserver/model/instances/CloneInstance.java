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
package l2next.gameserver.model.instances;

import l2next.commons.lang.reference.HardReference;
import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.model.ClonePlayer;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.GameObjectTasks;
import l2next.gameserver.model.Playable;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.templates.player.PlayerTemplate;
import l2next.gameserver.utils.Location;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CloneInstance extends ClonePlayer
{
	/**
	 * Field serialVersionUID. (value is -3990686488577795700)
	 */
	private static final long serialVersionUID = -3990686488577795700L;
	/**
	 * Field _owner.
	 */
	private final Player _owner;
	/**
	 * Field _lifetimeCountdown.
	 */
	private final int _lifetimeCountdown;
	/**
	 * Field _targetTask.
	 */
	private ScheduledFuture<?> _targetTask;
	/**
	 * Field _destroyTask.
	 */
	private ScheduledFuture<?> _destroyTask;

	/**
	 * Constructor for TreeInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 * @param lifetime int
	 * @param skill Skill
	 * @param loc Location
	 */
	public CloneInstance(int objectId, PlayerTemplate template, Player owner, int lifetime, Location loc)
	{
		super(objectId, template, owner);
		_owner = owner;
		_lifetimeCountdown = lifetime;
		setLoc(loc);
		setHeading(owner.getHeading());
	}

	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		_destroyTask = ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(this), _lifetimeCountdown);
		//_targetTask = EffectTaskManager.getInstance().scheduleAtFixedRate(new TargetTask(this), 10000L, 10000L);
	}

	/**
	 * @author Mobius
	 */
	private static class TargetTask extends RunnableImpl
	{
		/**
		 * Field _trapRef.
		 */
		private final HardReference<? extends Playable> _trapRef;

		/**
		 * Constructor for CastTask.
		 * @param trap TreeInstance
		 */
		public TargetTask(CloneInstance trap)
		{
			_trapRef = trap.getRef();
		}

		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			CloneInstance clone = (CloneInstance) _trapRef.get();
			if(clone == null)
			{
				return;
			}
			Player owner = clone.getPlayer();
			if(owner == null)
			{
				return;
			}
			if(!owner.isAttackingNow())
			{
				clone.abortAttack(true, false);
				clone.followToCharacter(owner, 70, false);
			}
		}
	}

	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
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
		super.onDelete();
	}

	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		if(_destroyTask != null)
		{
			_destroyTask.cancel(false);
			_destroyTask = null;
		}
		if(_targetTask != null)
		{
			_targetTask.cancel(false);
		}
	}

	/**
	 * Method stopDisappear.
	 */
	protected synchronized void stopDisappear()
	{
		if(_destroyTask != null)
		{
			_destroyTask.cancel(false);
			_destroyTask = null;
		}
		if(_targetTask != null)
		{
			_targetTask.cancel(false);
		}
	}

	/**
	 * Method displayGiveDamageMessage.
	 * @param target Creature
	 * @param damage int
	 * @param crit boolean
	 * @param miss boolean
	 * @param shld boolean
	 * @param magic boolean
	 */
	@Override
	public void displayGiveDamageMessage(Creature target, int damage, boolean crit, boolean miss, boolean shld, boolean magic)
	{
		Player owner = getPlayer();
		if(owner == null)
		{
			return;
		}
		if(crit)
		{
			owner.sendPacket(SystemMsg.SUMMONED_MONSTERS_CRITICAL_HIT);
		}
		if(miss)
		{
			owner.sendPacket(new SystemMessage(SystemMessage.C1S_ATTACK_WENT_ASTRAY).addString("Clone of " + owner.getName()));
		}
		else if(!target.isInvul())
		{
			owner.sendPacket(new SystemMessage(SystemMessage.C1_HAS_GIVEN_C2_DAMAGE_OF_S3).addString("Clone of " + owner.getName()).addName(target).addNumber(damage));
		}
	}

	/**
	 * Method displayReceiveDamageMessage.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	public void displayReceiveDamageMessage(Creature attacker, int damage)
	{
		Player owner = getPlayer();
		owner.sendPacket(new SystemMessage(SystemMessage.C1_HAS_RECEIVED_DAMAGE_OF_S3_FROM_C2).addString("Clone of " + owner.getName()).addName(attacker).addNumber((long) damage));
	}

	@Override
	public int getLevel()
	{
		return _owner.getLevel();
	}

	/**
	 * Method isServitor.
	 * @return boolean
	 */
	//@Override
	public boolean isServitor()
	{
		return true;
	}

	/**
	 * Method onAction.
	 * @param player Player
	 * @param shift boolean
	 */
	@Override
	public void onAction(Player player, boolean shift)
	{
		super.onAction(player, shift);
		if(shift)
		{
			if(!player.getPlayerAccess().CanViewChar)
			{
				return;
			}
			String dialog;
			dialog = HtmCache.getInstance().getNotNull("scripts/actions/admin.L2SummonInstance.onActionShift.htm", player);
			dialog = dialog.replaceFirst("%name%", String.valueOf(getPlayer().getName()));
			dialog = dialog.replaceFirst("%level%", String.valueOf(getLevel()));
			dialog = dialog.replaceFirst("%class%", String.valueOf(getPlayer().getClass().getSimpleName()));
			dialog = dialog.replaceFirst("%xyz%", getLoc().x + " " + getLoc().y + " " + getLoc().z);
			dialog = dialog.replaceFirst("%heading%", String.valueOf(getLoc().h));
			dialog = dialog.replaceFirst("%owner%", String.valueOf(getPlayer().getName()));
			dialog = dialog.replaceFirst("%ownerId%", String.valueOf(getPlayer().getObjectId()));
			dialog = dialog.replaceFirst("%npcId%", String.valueOf(0));
			dialog = dialog.replaceFirst("%expPenalty%", String.valueOf(0));
			dialog = dialog.replaceFirst("%maxHp%", String.valueOf(getMaxHp()));
			dialog = dialog.replaceFirst("%maxMp%", String.valueOf(getMaxMp()));
			dialog = dialog.replaceFirst("%currHp%", String.valueOf((int) getCurrentHp()));
			dialog = dialog.replaceFirst("%currMp%", String.valueOf((int) getCurrentMp()));
			dialog = dialog.replaceFirst("%pDef%", String.valueOf(getPDef(null)));
			dialog = dialog.replaceFirst("%mDef%", String.valueOf(getMDef(null, null)));
			dialog = dialog.replaceFirst("%pAtk%", String.valueOf(getPAtk(null)));
			dialog = dialog.replaceFirst("%mAtk%", String.valueOf(getMAtk(null, null)));
			dialog = dialog.replaceFirst("%accuracy%", String.valueOf(getAccuracy()));
			dialog = dialog.replaceFirst("%evasionRate%", String.valueOf(getEvasionRate(null)));
			dialog = dialog.replaceFirst("%crt%", String.valueOf(getCriticalHit(null, null)));
			dialog = dialog.replaceFirst("%runSpeed%", String.valueOf(getRunSpeed()));
			dialog = dialog.replaceFirst("%walkSpeed%", String.valueOf(getWalkSpeed()));
			dialog = dialog.replaceFirst("%pAtkSpd%", String.valueOf(getPAtkSpd()));
			dialog = dialog.replaceFirst("%mAtkSpd%", String.valueOf(getMAtkSpd()));
			dialog = dialog.replaceFirst("%dist%", String.valueOf(0));
			dialog = dialog.replaceFirst("%STR%", String.valueOf(getSTR()));
			dialog = dialog.replaceFirst("%DEX%", String.valueOf(getDEX()));
			dialog = dialog.replaceFirst("%CON%", String.valueOf(getCON()));
			dialog = dialog.replaceFirst("%INT%", String.valueOf(getINT()));
			dialog = dialog.replaceFirst("%WIT%", String.valueOf(getWIT()));
			dialog = dialog.replaceFirst("%MEN%", String.valueOf(getMEN()));
			NpcHtmlMessage msg = new NpcHtmlMessage(5);
			msg.setHtml(dialog);
			player.sendPacket(msg);
		}
	}

	/**
	 * Method getDEX.
	 * @return int
	 */
	@Override
	public int getDEX()
	{
		return _owner.getDEX();
	}

	/**
	 * Method getCON.
	 * @return int
	 */
	@Override
	public int getCON()
	{
		return _owner.getCON();
	}

	/**
	 * Method getINT.
	 * @return int
	 */
	@Override
	public int getINT()
	{
		return _owner.getINT();
	}

	/**
	 * Method getMEN.
	 * @return int
	 */
	@Override
	public int getMEN()
	{
		return _owner.getMEN();
	}

	/**
	 * Method getSTR.
	 * @return int
	 */
	@Override
	public int getSTR()
	{
		return _owner.getSTR();
	}

	/**
	 * Method getEvasionRate.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getEvasionRate(Creature target)
	{
		return _owner.getEvasionRate(target);
	}

	/**
	 * Method getMEvasionRate.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getMEvasionRate(Creature target)
	{
		return _owner.getMEvasionRate(target);
	}

	/**
	 * Method getWIT.
	 * @return int
	 */
	@Override
	public int getWIT()
	{
		return _owner.getWIT();
	}

	/**
	 * Method getMAtk.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getMAtk(Creature target, Skill skill)
	{
		return _owner.getMAtk(target, skill);
	}

	/**
	 * Method getMAtkSpd.
	 * @return int
	 */
	@Override
	public int getMAtkSpd()
	{
		return _owner.getMAtkSpd();
	}

	/**
	 * Method getMaxCp.
	 * @return int
	 */
	@Override
	public int getMaxCp()
	{
		return _owner.getMaxCp();
	}

	/**
	 * Method getMaxHp.
	 * @return int
	 */
	@Override
	public int getMaxHp()
	{
		return _owner.getMaxHp();
	}

	/**
	 * Method getMaxMp.
	 * @return int
	 */
	@Override
	public int getMaxMp()
	{
		return _owner.getMaxMp();
	}

	/**
	 * Method getMDef.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getMDef(Creature target, Skill skill)
	{
		return _owner.getMDef(target, skill);
	}

	/**
	 * Method getMovementSpeedMultiplier.
	 * @return double
	 */
	@Override
	public double getMovementSpeedMultiplier()
	{
		return _owner.getMovementSpeedMultiplier();
	}

	/**
	 * Method getRunSpeed.
	 * @return int
	 */
	@Override
	public int getRunSpeed()
	{
		return _owner.getRunSpeed();
	}

	/**
	 * Method getCriticalHit.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getCriticalHit(Creature target, Skill skill)
	{
		return _owner.getCriticalHit(target, skill);
	}

	/**
	 * Method getWalkSpeed.
	 * @return int
	 */
	@Override
	public int getWalkSpeed()
	{
		return _owner.getWalkSpeed();
	}

	/**
	 * Method getSwimRunSpeed.
	 * @return int
	 */
	@Override
	public int getSwimRunSpeed()
	{
		return _owner.getSwimRunSpeed();
	}

	/**
	 * Method getSwimWalkSpeed.
	 * @return int
	 */
	@Override
	public int getSwimWalkSpeed()
	{
		return _owner.getSwimWalkSpeed();
	}

	@Override
	public int getPAtk(Creature target)
	{
		return _owner.getPAtk(target);
	}

	/**
	 * Method getPAtkSpd.
	 * @return int
	 */
	@Override
	public int getPAtkSpd()
	{
		return _owner.getPAtkSpd();
	}

	/**
	 * Method getPDef.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getPDef(Creature target)
	{
		return _owner.getPDef(target);
	}

}
