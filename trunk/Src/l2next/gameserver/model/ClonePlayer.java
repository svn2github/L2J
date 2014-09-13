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
package l2next.gameserver.model;

import l2next.commons.threading.RunnableImpl;
import l2next.commons.util.Rnd;
import l2next.gameserver.Config;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.ai.ClonePlayerAI;
import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.instancemanager.ReflectionManager;
import l2next.gameserver.listener.actor.OnAttackListener;
import l2next.gameserver.listener.actor.OnMagicUseListener;
import l2next.gameserver.model.base.TeamType;
import l2next.gameserver.model.entity.events.GlobalEvent;
import l2next.gameserver.model.items.Inventory;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.party.Party;
import l2next.gameserver.network.serverpackets.ActionFail;
import l2next.gameserver.network.serverpackets.AutoAttackStart;
import l2next.gameserver.network.serverpackets.CharInfo;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.network.serverpackets.MyTargetSelected;
import l2next.gameserver.network.serverpackets.PartySpelled;
import l2next.gameserver.network.serverpackets.RelationChanged;
import l2next.gameserver.network.serverpackets.StatusUpdate;
import l2next.gameserver.scripts.Events;
import l2next.gameserver.templates.item.WeaponTemplate;
import l2next.gameserver.templates.player.PlayerTemplate;
import l2next.gameserver.utils.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClonePlayer extends Playable
{
	/**
	 * Field serialVersionUID. (value is -7275714049223105460)
	 */
	private static final long serialVersionUID = -7275714049223105460L;
	/**
	 * Field SUMMON_DISAPPEAR_RANGE. (value is 2500)
	 */
	private static final int SUMMON_DISAPPEAR_RANGE = 2500;
	/**
	 * Field _owner.
	 */
	private final Player _owner;
	/**
	 * Field _spsCharged.
	 */
	private int _spsCharged;
	/**
	 * Field _ssCharged. Field _follow.
	 */
	private boolean _follow = true, _ssCharged = false;
	private final OwnerAttakListener _listener;

	/**
	 * Constructor for FakePlayer.
	 * @param objectId int
	 * @param template PlayerTemplate
	 * @param owner Player
	 */
	public ClonePlayer(int objectId, PlayerTemplate template, Player owner)
	{
		super(objectId, template);
		_owner = owner;
		_listener = new OwnerAttakListener();
		owner.addListener(_listener);
		setXYZ(owner.getX() + Rnd.get(-100, 100), owner.getY() + Rnd.get(-100, 100), owner.getZ());
	}

	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		getPlayer();
		getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
	}

	/**
	 * Method getAI.
	 * @return ClonePlayerAI
	 */
	@Override
	public ClonePlayerAI getAI()
	{
		if(_ai == null)
		{
			synchronized(this)
			{
				if(_ai == null)
				{
					_ai = new ClonePlayerAI(this);
				}
			}
		}
		return (ClonePlayerAI) _ai;
	}

	/**
	 * Method onAction.
	 * @param player Player
	 * @param shift boolean
	 */
	@Override
	public void onAction(final Player player, boolean shift)
	{
		if(isFrozen())
		{
			player.sendPacket(ActionFail.STATIC);
			return;
		}
		if(Events.onAction(player, this, shift))
		{
			player.sendPacket(ActionFail.STATIC);
			return;
		}
		Player owner = getPlayer();
		if(player.getTarget() != this)
		{
			player.setTarget(this);
			if(player.getTarget() == this)
			{
				player.sendPacket(new MyTargetSelected(getObjectId(), 0), makeStatusUpdate(StatusUpdate.CUR_HP, StatusUpdate.MAX_HP, StatusUpdate.CUR_MP, StatusUpdate.MAX_MP));
			}
			else
			{
				player.sendPacket(ActionFail.STATIC);
			}
		}
		else if(player == owner)
		{
			player.sendPacket(new CharInfo(this));
			player.sendPacket(ActionFail.STATIC);
		}
		else if(isAutoAttackable(player))
		{
			player.getAI().Attack(this, false, shift);
		}
		else
		{
			if(player.getAI().getIntention() != CtrlIntention.AI_INTENTION_FOLLOW)
			{
				if(!shift)
				{
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, this, Config.FOLLOW_RANGE);
				}
				else
				{
					player.sendActionFailed();
				}
			}
			else
			{
				player.sendActionFailed();
			}
		}
	}

	/**
	 * Method setFollowMode.
	 * @param state boolean
	 */
	public void setFollowMode(boolean state)
	{
		Player owner = getPlayer();
		_follow = state;
		if(_follow)
		{
			if(getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
			{
				getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, owner, Config.FOLLOW_RANGE);
			}
		}
		else if(getAI().getIntention() == CtrlIntention.AI_INTENTION_FOLLOW)
		{
			getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		}
	}

	/**
	 * Method isFollowMode.
	 * @return boolean
	 */
	public boolean isFollowMode()
	{
		return _follow;
	}

	/**
	 * Method isClone.
	 * @return boolean
	 */
	//@Override
	public boolean isClone()
	{
		return true;
	}

	/**
	 * Field _updateEffectIconsTask.
	 */
	Future<?> _updateEffectIconsTask;

	/**
	 * @author Mobius
	 */
	private class UpdateEffectIcons extends RunnableImpl
	{
		/**
		 * Constructor for UpdateEffectIcons.
		 */
		public UpdateEffectIcons()
		{
			// TODO Auto-generated constructor stub
		}

		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			updateEffectIconsImpl();
			_updateEffectIconsTask = null;
		}
	}

	/**
	 * Method updateEffectIcons.
	 */
	@Override
	public void updateEffectIcons()
	{
		super.updateEffectIcons();
		if(Config.USER_INFO_INTERVAL == 0)
		{
			if(_updateEffectIconsTask != null)
			{
				_updateEffectIconsTask.cancel(false);
				_updateEffectIconsTask = null;
			}
			updateEffectIconsImpl();
			return;
		}
		if(_updateEffectIconsTask != null)
		{
			return;
		}
		_updateEffectIconsTask = ThreadPoolManager.getInstance().schedule(new UpdateEffectIcons(), Config.USER_INFO_INTERVAL);
	}

	/**
	 * Method updateEffectIconsImpl.
	 */
	public void updateEffectIconsImpl()
	{
		Player owner = getPlayer();
		PartySpelled ps = new PartySpelled(this, true);
		Party party = owner.getParty();
		if(party != null)
		{
			party.broadCast(ps);
		}
		else
		{
			owner.sendPacket(ps);
		}
	}

	/**
	 * Method getActiveWeaponInstance.
	 * @return ItemInstance
	 */
	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return _owner.getActiveWeaponInstance();
	}

	/**
	 * Method getActiveWeaponItem.
	 * @return WeaponTemplate
	 */
	@Override
	public WeaponTemplate getActiveWeaponItem()
	{
		return _owner.getActiveWeaponItem();
	}

	/**
	 * Method getSecondaryWeaponInstance.
	 * @return ItemInstance
	 */
	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return _owner.getSecondaryWeaponInstance();
	}

	/**
	 * Method getSecondaryWeaponItem.
	 * @return WeaponTemplate
	 */
	@Override
	public WeaponTemplate getSecondaryWeaponItem()
	{
		return _owner.getSecondaryWeaponItem();
	}

	/**
	 * Method unChargeShots.
	 * @param spirit boolean
	 * @return boolean
	 */
	@Override
	public boolean unChargeShots(final boolean spirit)
	{
		Player owner = getPlayer();
		if(spirit)
		{
			if(_spsCharged != 0)
			{
				_spsCharged = 0;
				owner.autoShot();
				return true;
			}
		}
		else if(_ssCharged)
		{
			_ssCharged = false;
			owner.autoShot();
			return true;
		}
		return false;
	}

	/**
	 * Method getChargedSoulShot.
	 * @return boolean
	 */
	@Override
	public boolean getChargedSoulShot()
	{
		return _ssCharged;
	}

	/**
	 * Method getChargedSpiritShot.
	 * @return int
	 */
	@Override
	public int getChargedSpiritShot()
	{
		return _spsCharged;
	}

	/**
	 * Method chargeSoulShot.
	 */
	public void chargeSoulShot()
	{
		_ssCharged = true;
	}

	/**
	 * Method chargeSpiritShot.
	 * @param state int
	 */
	public void chargeSpiritShot(final int state)
	{
		_spsCharged = state;
	}

	/**
	 * Method isInRange.
	 * @return boolean
	 */
	public boolean isInRange()
	{
		Player owner = getPlayer();
		return getDistance(owner) < SUMMON_DISAPPEAR_RANGE;
	}

	/**
	 * Method teleportToOwner.
	 */
	public void teleportToOwner()
	{
		Player owner = getPlayer();
		setNonAggroTime(System.currentTimeMillis() + Config.NONAGGRO_TIME_ONTELEPORT);
		if(owner.isInOlympiadMode())
		{
			teleToLocation(owner.getLoc(), owner.getReflection());
		}
		else
		{
			teleToLocation(Location.findPointToStay(owner, 50, 150), owner.getReflection());
		}
		if(!isDead() && _follow)
		{
			getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, owner, Config.FOLLOW_RANGE);
		}
	}

	/**
	 * Field _broadcastCharInfoTask.
	 */
	ScheduledFuture<?> _broadcastCharInfoTask;

	/**
	 * @author Mobius
	 */
	public class BroadcastCharInfoTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			broadcastCharInfoImpl();
			_broadcastCharInfoTask = null;
		}
	}

	/**
	 * Method broadcastCharInfo.
	 */
	@Override
	public void broadcastCharInfo()
	{
		if(_broadcastCharInfoTask != null)
		{
			return;
		}
		_broadcastCharInfoTask = ThreadPoolManager.getInstance().schedule(new BroadcastCharInfoTask(), Config.BROADCAST_CHAR_INFO_INTERVAL);
	}

	/**
	 * Method broadcastCharInfoImpl.
	 */
	public void broadcastCharInfoImpl()
	{
		for(Player player : World.getAroundPlayers(this))
		{
			player.sendPacket(new CharInfo(this));
		}
	}

	/**
	 * Method startPvPFlag.
	 * @param target Creature
	 */
	@Override
	public void startPvPFlag(Creature target)
	{
		Player owner = getPlayer();
		owner.startPvPFlag(target);
	}

	/**
	 * Method getPvpFlag.
	 * @return int
	 */
	@Override
	public int getPvpFlag()
	{
		Player owner = getPlayer();
		return owner.getPvpFlag();
	}

	/**
	 * Method getKarma.
	 * @return int
	 */
	@Override
	public int getKarma()
	{
		Player owner = getPlayer();
		return owner.getKarma();
	}

	/**
	 * Method getTeam.
	 * @return TeamType
	 */
	@Override
	public TeamType getTeam()
	{
		Player owner = getPlayer();
		return owner.getTeam();
	}

	/**
	 * Method getPlayer.
	 * @return Player
	 */
	@Override
	public Player getPlayer()
	{
		return _owner;
	}

	/**
	 * Method addPacketList.
	 * @param forPlayer Player
	 * @param dropper Creature
	 * @return List<L2GameServerPacket>
	 */
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		//List<L2GameServerPacket> list = new ArrayList<>();
		List<L2GameServerPacket> list = new ArrayList<L2GameServerPacket>();
		Player owner = getPlayer();
		if(owner == forPlayer)
		{
			list.add(new CharInfo(this));
		}
		else
		{
			Party party = forPlayer.getParty();
			if((getReflection() == ReflectionManager.GIRAN_HARBOR) && ((owner == null) || (party == null) || (party != owner.getParty())))
			{
				return list;
			}
			list.add(new CharInfo(this));
			list.add(RelationChanged.update(forPlayer, this, forPlayer));
		}
		if(isInCombat())
		{
			list.add(new AutoAttackStart(getObjectId()));
		}
		if(isMoving || isFollow)
		{
			list.add(movePacket());
		}
		return list;
	}

	/**
	 * Method startAttackStanceTask.
	 */
	//@Override
	public void startAttackStanceTask0()
	{
		//startAttackStanceTask0();
		Creature player = getPlayer();
		if(player != null)
		{
			player.startAttackStanceTask();
		}
	}

	/**
	 * Method getEvent.
	 * @param eventClass Class<E>
	 * @return E
	 */
	@Override
	public <E extends GlobalEvent> E getEvent(Class<E> eventClass)
	{
		Player player = getPlayer();
		if(player != null)
		{
			return player.getEvent(eventClass);
		}
		return super.getEvent(eventClass);
	}

	/**
	 * Method getEvents.
	 * @return Set<GlobalEvent>
	 */
	@Override
	public Set<GlobalEvent> getEvents()
	{
		Player player = getPlayer();
		if(player != null)
		{
			return player.getEvents();
		}
		return super.getEvents();
	}

	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return false;
	}

	/**
	 * Method getLevel.
	 * @return int
	 */
	@Override
	public int getLevel()
	{
		return _owner.getLevel();
	}

	/**
	 * Method notifyOwerStartAttak.
	 */
	public void notifyOwnerStartAttak()
	{
		getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, _owner.getTarget());
	}

	/**
	 * @author Mobius
	 */
	private class OwnerAttakListener implements OnAttackListener, OnMagicUseListener
	{
		/**
		 * Constructor for OwnerAttakListener.
		 */
		public OwnerAttakListener()
		{
			// TODO Auto-generated constructor stub
		}

		/**
		 * Method onMagicUse.
		 * @param actor Creature
		 * @param skill Skill
		 * @param target Creature
		 * @param alt boolean
		 * @see l2next.gameserver.listener.actor.OnMagicUseListener#onMagicUse(Creature, Skill, Creature, boolean)
		 */
		@Override
		public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
		{
			notifyOwnerStartAttak();
		}

		/**
		 * Method onAttack.
		 * @param actor Creature
		 * @param target Creature
		 * @see l2next.gameserver.listener.actor.OnAttackListener#onAttack(Creature, Creature)
		 */
		@Override
		public void onAttack(Creature actor, Creature target)
		{
			notifyOwnerStartAttak();
		}
	}

	@Override
	public Inventory getInventory()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getWearedMask()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void doPickupItem(GameObject object)
	{
		// TODO Auto-generated method stub

	}
}
