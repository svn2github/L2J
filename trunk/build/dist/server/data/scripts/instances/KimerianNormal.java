package instances;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.instancemanager.SpawnManager;
import l2next.gameserver.listener.actor.OnDeathListener;
import l2next.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Zone;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.ExSendUIEvent;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.utils.NpcUtils;
import l2next.gameserver.utils.Location;

/**
 * Класс инстанса для прохождения квеста "Падший Лидер".
 *
 * @author Awakeninger
 */

public class KimerianNormal extends Reflection
{
	private ZoneListener _epicZoneListener = new ZoneListener();
	private DeathListener _deathListener = new DeathListener();
	private KimerianNormalState _state;
	
	public static enum KimerianNormalState
	{
		Start,
		Stop;
	}
	
	private void setState(KimerianNormalState state)
	{
		_state = state;
	}
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[Kimerian]").addListener(_epicZoneListener);
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		NpcInstance Kimerian = addSpawnWithoutRespawn(25747, new Location(224216, 70968, 1610, 26634), 0);
		Kimerian.addListener(_deathListener);
		setState(KimerianNormalState.Start);
		NpcUtils.spawnSingle(33098, new Location(215688,79784,798,26634), player.getActiveReflection(), 10 * 1000L);
		NpcUtils.spawnSingle(33097, new Location(215592,80056,800,0), player.getActiveReflection());
		for(int i = 0;i < 3;i++)
		{
			NpcUtils.spawnSingle(32913, new Location(215592,80056,800,0), player.getActiveReflection());
		}
	}
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			NpcUtils.spawnSingle(25745, new Location(217288,78600,932,27931), player.getActiveReflection(), 10 * 1000L);
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc() && self.getNpcId() == 25747)
			{
				for(Player p : getPlayers())
				{
					p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
					startCollapseTimer(5 * 60 * 1000L);
					setReenterTime(System.currentTimeMillis());
				}
			}
		}
	}
}