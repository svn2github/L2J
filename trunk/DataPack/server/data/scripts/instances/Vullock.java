package instances;

import l2next.commons.geometry.Polygon;
import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.listener.actor.OnCurrentHpDamageListener;
import l2next.gameserver.listener.actor.OnDeathListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Territory;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.SceneMovie;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс контролирует Валлока, рб 99 лвл
 *
 * @author Awakeninger
 * Логгеры поставлены исключительно на время тестов.
 * Чтобы видеть стадии инстанса.
 *
 * По факту - это просто болванка инстанса.
 */

public class Vullock extends Reflection
{
	private static final Logger _log = LoggerFactory.getLogger(Vullock.class);
	private static final int Vullock = 29218;
	private static final int VullockSlave = 29219;
	private static final int Golem = 23123;
	private Player player;
	private Location Golem1Loc = new Location(152712, 142936, -12762, 57343);
	private Location Golem2Loc = new Location(154360, 142936, -12762, 40959);
	private Location Golem3Loc = new Location(154360, 141288, -12762, 25029);
	private Location Golem4Loc = new Location(152712, 141288, -12762, 8740);
	private Location vullockspawn = new Location(153576, 142088, -12762, 16383);
	private DeathListener _deathListener = new DeathListener();
	private CurrentHpListener _currentHpListener = new CurrentHpListener();
	private static final long BeforeDelay = 60 * 1000L;
	private static final long BeforeDelayVDO = 42 * 1000L;
	private static Territory centralRoomPoint = new Territory().add(new Polygon().add(152712, 142936).add(154360, 142936).add(154360, 141288).add(152712, 141288).setZmax(-12862).setZmin(-12700));
	private AtomicInteger raidplayers = new AtomicInteger();

	private boolean checkstartCond(int raidplayers)
	{
		return !(raidplayers < getInstancedZone().getMinParty());
	}

	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		if(checkstartCond(raidplayers.incrementAndGet()))
		{
			ThreadPoolManager.getInstance().schedule(new FirstStage(), BeforeDelayVDO);
			ThreadPoolManager.getInstance().schedule(new VullockSpawn(this), BeforeDelay);
		}

	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc() && self.getNpcId() == Vullock)
			{
				setReenterTime(System.currentTimeMillis());
				for(Player p : getPlayers())
				{
					p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
				}
				startCollapseTimer(5 * 60 * 1000L);
			}
		}
	}

	private class FirstStage extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			for(Player player : getPlayers())
			{
				player.showQuestMovie(SceneMovie.si_barlog_opening);
			}
		}
	}

	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if(actor.getNpcId() == Vullock)
			{
				if(actor == null || actor.isDead())
				{
					return;
				}

				if(actor.getCurrentHp() <= 7850000)
				{
					ThreadPoolManager.getInstance().schedule(new CaveStage(), 0);
					actor.removeListener(_currentHpListener);
				}
			}
		}

	}

	public class CaveStage extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			NpcInstance Slave1 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			Slave1.getAI().Attack(player, true, false);
			NpcInstance Slave2 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			Slave2.getAI().Attack(player, true, false);
			NpcInstance Slave3 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			Slave3.getAI().Attack(player, true, false);
			NpcInstance Slave4 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			Slave4.getAI().Attack(player, true, false);
			NpcInstance Slave5 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			Slave5.getAI().Attack(player, true, false);
			NpcInstance Slave6 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			Slave6.getAI().Attack(player, true, false);

		}
	}

	public class VullockSpawn extends RunnableImpl
	{
		Reflection _r;

		public VullockSpawn(Reflection r)
		{
			_r = r;
		}

		@Override
		public void runImpl()
		{
			Location Loc1 = Golem1Loc;
			Location Loc2 = Golem2Loc;
			Location Loc3 = Golem3Loc;
			Location Loc4 = Golem4Loc;
			Location Loc = vullockspawn;
			NpcInstance VullockStay = addSpawnWithoutRespawn(Vullock, Loc, 0);
			VullockStay.addListener(_deathListener);
			VullockStay.addListener(_currentHpListener);
			_r.addSpawnWithoutRespawn(Golem, Loc1, 0);
			_r.addSpawnWithoutRespawn(Golem, Loc2, 0);
			_r.addSpawnWithoutRespawn(Golem, Loc3, 0);
			_r.addSpawnWithoutRespawn(Golem, Loc4, 0);
		}
	}
}