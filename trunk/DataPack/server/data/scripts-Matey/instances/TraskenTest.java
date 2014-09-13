package instances;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.listener.actor.OnCurrentHpDamageListener;
import l2next.gameserver.listener.actor.OnDeathListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.ExSendUIEvent;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.utils.Location;

/**
 * @author Awakeninger
 */

public class TraskenTest extends Reflection
{
	private static final int tail = 29222;
	private static final int trasken = 29197;
	private static final int lich = 29204;
	private static final int shup = 29198;
	private static final int shup_big = 29199;
	private static final int zhertva = 29201;
	private static final int tradgan = 19160;
	
	private Location tail_loc1 = new Location(81256, -180232, -9900);
	private Location tail_loc2 = new Location(78984, -181672, -9901);
	private Location tail_loc3 = new Location(80536, -183800, -9900);
	private Location tail_loc4 = new Location(83048, -181960, -9922);
	private Location trasken_loc = new Location(81208, -182088, -9917);
	private Location[] shup_loc = { new Location(81640, -181048, -9901),new Location(81224, -180232, -9900),new Location(78968, -181688, -9900),new Location(79256, -182456, -9902),new Location(79736, -181832, -9900),new Location(80424, -181368, -9902), new Location(80168, -182472, -9900),new Location(80792, -182920, -9900)};
	
	private CurrentHpListener _currentHpListener = new CurrentHpListener();
	private DeathListener _deathListener = new DeathListener();
	
	private int _state = 0;
		
	private void setStage(int state)
	{
		_state = state;
	}
	
	private int getStage()
	{
		return _state;
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		ThreadPoolManager.getInstance().schedule(new SpawnTask(), 20 * 1000L);
		setStage(1);
	}
	
	private class SpawnTask extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			NpcInstance tail1 = addSpawnWithoutRespawn(tail, tail_loc1, 0);
			tail1.addListener(_currentHpListener);
			spawnShups();
		}
	}
	
	private void spawnShups()
	{
		for(Location loc : shup_loc)
		{
			addSpawnWithRespawn(shup, loc, 30, 0);
		}
	}
	
	private void spawnTail2()
	{
		NpcInstance tail2 = addSpawnWithoutRespawn(tail, tail_loc2, 0);
		tail2.addListener(_currentHpListener);
	}
	
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(final Creature actor, final double damage, final Creature attacker, Skill skill)
		{
			double HpPercent = actor.getCurrentHpPercents();
			if(actor.getNpcId() == tail && getStage() == 1)
			{
				if(HpPercent <= 95.)
				{
					setStage(2);
					actor.decayMe();
					spawnTail2();
				}
			}
		}
	}

	private class DeathListener implements OnDeathListener
	{
		private NpcInstance exchange;

		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc())
			{

			}
		}
	}
}