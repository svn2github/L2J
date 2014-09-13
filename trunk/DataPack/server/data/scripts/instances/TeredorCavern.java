package instances;

import l2next.gameserver.listener.actor.OnDeathListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.utils.Location;

public class TeredorCavern extends Reflection
{
	private static int Teredor = 25785;
	private static Location TeredorSpawnCoords = new Location(175176, -185128, -3822);
	private static final int ADVENTURE_GUILDSMAN = 33385;
	private DeathListener _deathListener = new DeathListener();

	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		NpcInstance Ter = addSpawnWithoutRespawn(Teredor, TeredorSpawnCoords, 1);
		Ter.addListener(_deathListener);
	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc() && self.getNpcId() == Teredor)
			{
				for(Player p : getPlayers())
				{
					p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
				}
				startCollapseTimer(5 * 60 * 1000L);
				NpcInstance GuildsMan = addSpawnWithoutRespawn(ADVENTURE_GUILDSMAN, TeredorSpawnCoords, 1);
				setReenterTime(System.currentTimeMillis());
			}
		}
	}
}