package instances;

import l2next.gameserver.listener.actor.OnDeathListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.ExSendUIEvent;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.utils.Location;

/**
 * Класс инстанса на парнасе.
 *
 * @author Awakeninger
 */

public class CrystalHall extends Reflection
{
	private static final int RB1 = 25881;
	private static final int RB2 = 25881;
	private static final int Сannon = 19008;
	private static final int Cannon8 = 19009;//После нее открываем двери
	private static final int Exchanger = 33386;
	private static final int DoorOutside = 24220005;
	private static final int DoorInside = 24220006;
	private long _savedTime = System.currentTimeMillis();
	private Location Сannon1Loc = new Location(143144, 145832, -12061);
	private Location Сannon2Loc = new Location(141912, 144200, -11949);
	private Location Сannon3Loc = new Location(143368, 143768, -11976);
	private Location Сannon4Loc = new Location(145544, 143746, -11841);
	private Location Сannon5Loc = new Location(147544, 144872, -12251);
	private Location Сannon6Loc = new Location(148952, 145224, -12326);
	private Location Сannon7Loc = new Location(148152, 146136, -12305);
	private Location Сannon8Loc = new Location(149096, 146872, -12369);
	private Location RB1Loc = new Location(152984, 145960, -12609, 15640);
	private Location RB2Loc = new Location(152536, 145960, -12609, 15640);
	private DeathListener _deathListener = new DeathListener();
	private CrystallHallState _state;
	private int time = (int) (System.currentTimeMillis() - _savedTime) / 1000;
	
	public static enum CrystallHallState
	{
		Start, 
		Cannon1, 
		Cannon2,
		Cannon3,
		Cannon4,
		Cannon5,
		Cannon6,
		Cannon7,
		Cannon8,
		Emambithy1,
		Emambithy2,
		Stop;
	}
	
	private void setState(CrystallHallState state)
	{
		_state = state;
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		player.sendPacket(new ExSendUIEvent(player, 0, 1, time, 0, NpcString.ELAPSED_TIME));
		NpcInstance can8 = addSpawnWithoutRespawn(Cannon8, Сannon8Loc, 0);
		setState(CrystallHallState.Start);
		can8.addListener(_deathListener);
		NpcInstance can1 = addSpawnWithoutRespawn(Сannon, Сannon1Loc, 0);
		can1.addListener(_deathListener);
		NpcInstance can2 = addSpawnWithoutRespawn(Сannon, Сannon2Loc, 0);
		can2.addListener(_deathListener);
		NpcInstance can3 = addSpawnWithoutRespawn(Сannon, Сannon3Loc, 0);
		can3.addListener(_deathListener);
		NpcInstance can4 = addSpawnWithoutRespawn(Сannon, Сannon4Loc, 0);
		can4.addListener(_deathListener);
		NpcInstance can5 = addSpawnWithoutRespawn(Сannon, Сannon5Loc, 0);
		can5.addListener(_deathListener);
		NpcInstance can6 = addSpawnWithoutRespawn(Сannon, Сannon6Loc, 0);
		can6.addListener(_deathListener);
		NpcInstance can7 = addSpawnWithoutRespawn(Сannon, Сannon7Loc, 0);
		can7.addListener(_deathListener);
		NpcInstance RB1N = addSpawnWithoutRespawn(RB1, RB1Loc, 0);
		RB1N.addListener(_deathListener);
		NpcInstance RB2N = addSpawnWithoutRespawn(RB2, RB2Loc, 0);
		RB2N.addListener(_deathListener);
	}

	private class DeathListener implements OnDeathListener
	{
		private NpcInstance exchange;

		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc() && self.getNpcId() == Сannon)
			{
				for(Player p : getPlayers())
				{
					if(_state == CrystallHallState.Start)
					{
						p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "1"));
						setState(CrystallHallState.Cannon1);
					}
					else if(_state == CrystallHallState.Cannon1)
					{
						p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "2"));
						setState(CrystallHallState.Cannon2);
					}
					else if(_state == CrystallHallState.Cannon2)
					{
						p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "3"));
						setState(CrystallHallState.Cannon3);
					}
					else if(_state == CrystallHallState.Cannon3)
					{
						p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "4"));
						setState(CrystallHallState.Cannon4);
					}
					else if(_state == CrystallHallState.Cannon4)
					{
						p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "5"));
						setState(CrystallHallState.Cannon5);
					}
					else if(_state == CrystallHallState.Cannon5)
					{
						p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "6"));
						setState(CrystallHallState.Cannon6);
					}
					else if(_state == CrystallHallState.Cannon6)
					{
						p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "7"));
						setState(CrystallHallState.Cannon7);
					}
				}
			}
			else if(self.isNpc() && self.getNpcId() == Cannon8)
			{
				for(Player p : getPlayers())
				{
					p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying_open_door, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true));
					setState(CrystallHallState.Emambithy1);
				}
				getDoor(DoorOutside).openMe();
				getDoor(DoorInside).openMe();
			}
			else if(self.isNpc() && self.getNpcId() == RB1)
			{
				for(Player p : getPlayers())
				{
					if(_state == CrystallHallState.Emambithy1)
					{
						setState(CrystallHallState.Emambithy2);
						return;
					}
					if(_state == CrystallHallState.Emambithy2)
					{
						p.sendPacket(new ExSendUIEvent(p, 1, 1, 0, 0));
						p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
						startCollapseTimer(5 * 60 * 1000L);
						exchange = addSpawnWithoutRespawn(Exchanger, RB2Loc, 0);
						setState(CrystallHallState.Stop);
						setReenterTime(System.currentTimeMillis());
						return;
					}
				}
			}
		}
	}
}