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

import java.util.concurrent.ScheduledFuture;

import l2next.commons.util.Rnd;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.data.xml.holder.NpcHolder;
import l2next.gameserver.model.SimpleSpawner;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Mammons extends Functions implements ScriptFile
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Mammons.class);
	/**
	 * Field MAMMON_PRIEST_ID. (value is 33511)
	 */
	private static final int MAMMON_PRIEST_ID = 33511;
	/**
	 * Field MAMMON_MERCHANT_ID. (value is 31113)
	 */
	private static final int MAMMON_MERCHANT_ID = 31113;
	/**
	 * Field MAMMON_BLACKSMITH_ID. (value is 31126)
	 */
	private static final int MAMMON_BLACKSMITH_ID = 31126;
	/**
	 * Field PORT_TIME.
	 */
	private static final int PORT_TIME = 4 * 60 * 60 * 1000;
	/**
	 * Field PriestNpc.
	 */
	static NpcInstance PriestNpc;
	/**
	 * Field MerchantNpc.
	 */
	static NpcInstance MerchantNpc;
	/**
	 * Field BlacksmithNpc.
	 */
	static NpcInstance BlacksmithNpc;
	/**
	 * Field _mammonTeleportTask.
	 */
	private static ScheduledFuture<?> _mammonTeleportTask;
	/**
	 * Field mammonText.
	 */
	static final NpcString[] mammonText =
	{
		NpcString.RULERS_OF_THE_SEAL_I_BRING_YOU_WONDROUS_GIFTS,
		NpcString.RULERS_OF_THE_SEAL_I_HAVE_SOME_EXCELLENT_WEAPONS_TO_SHOW_YOU,
		NpcString.IVE_BEEN_SO_BUSY_LATELY_IN_ADDITION_TO_PLANNING_MY_TRIP
	};
	/**
	 * Field MAMMON_PRIEST_POINTS.
	 */
	static final Location[] MAMMON_PRIEST_POINTS =
	{
		//new Location(16335, 144696, -3024, 27931),
		// Dion
		new Location(81284, 150155, -3536, 64087),
		// Giran
		//new Location(114478, 217596, -3632, 61496),
		// Heine
	//	new Location(-12136, 121784, -2992, 27433),
		// Gludio
	//	new Location(-84808, 151416, -3120, 59718),
		// Gludin
	//	new Location(79976, 56968, -1568, 61318),
		// Oren
	//	new Location(144664, -54200, -2976, 40707),
		// Goddard
	//	new Location(90376, -143544, -1536, 41834),
		// Shuttgard
		new Location(42792, -41384, -2192, 30607),
		// Rune
		new Location(146968, 29656, -2272, 28713),
		// Aden
		//new Location(120456, 76456, -2144, 30749),
		// Hunters
	};
	/**
	 * Field MAMMON_MERCHANT_POINTS.
	 */
	static final Location[] MAMMON_MERCHANT_POINTS =
	{
		//new Location(16344, 144792, -3024, 27931),
		// Dion
		new Location(81266, 150091, -3536, 64087),
		// Giran
		//new Location(114482, 217538, -3632, 61496),
		// Heine
	//	//new Location(-12120, 121848, -2992, 27433),
		// Gludio
		//new Location(-84808, 151352, -3120, 61318),
		// Gludin
		//new Location(79992, 56888, -1552, 59718),
		// Oren
		//new Location(144584, -54184, -2976, 40707),
		// Goddard
	//	new Location(90232, -143592, -1536, 41834),
		// Shuttgard
		new Location(42776, -41320, -2192, 30607),
		// Rune
		new Location(146968, 29704, -2272, 28713),
		// Aden
		//new Location(120440, 76504, -2144, 30749),
		// Hunters
	};
	/**
	 * Field MAMMON_BLACKSMITH_POINTS.
	 */
	static final Location[] MAMMON_BLACKSMITH_POINTS =
	{
		//new Location(16360, 144856, -3024, 27931),
		// Dion
		new Location(81272, 150041, -3536, 64087),
		// Giran
		//new Location(114484, 217462, -3632, 61496),
		// Heine
		//new Location(-12120, 121912, -2992, 27433),
		// Gludio
		//new Location(-84808, 151320, -3120, 59718),
		// Gludin
		//new Location(79992, 56792, -1552, 61318),
		// Oren
		//new Location(144520, -54152, -2976, 40707),
		// Goddard
		//new Location(89992, -143672, -1536, 41834),
		// Shuttgard
		new Location(42760, -41256, -2192, 30607),
		// Rune
		new Location(146968, 29784, -2272),
		// Aden
		//new Location(120408, 76568, -2144, 30749),
		// Hunters
	};
	
	/**
	 * Method SpawnMammons.
	 */
	public void SpawnMammons()
	{
		final int firstTown = Rnd.get(MAMMON_PRIEST_POINTS.length);
		NpcTemplate template = NpcHolder.getInstance().getTemplate(MAMMON_PRIEST_ID);
		SimpleSpawner sp = new SimpleSpawner(template);
		sp.setLoc(MAMMON_PRIEST_POINTS[firstTown]);
		sp.setAmount(1);
		sp.setRespawnDelay(0);
		PriestNpc = sp.doSpawn(true);
		template = NpcHolder.getInstance().getTemplate(MAMMON_MERCHANT_ID);
		sp = new SimpleSpawner(template);
		sp.setLoc(MAMMON_MERCHANT_POINTS[firstTown]);
		sp.setAmount(1);
		sp.setRespawnDelay(0);
		MerchantNpc = sp.doSpawn(true);
		template = NpcHolder.getInstance().getTemplate(MAMMON_BLACKSMITH_ID);
		sp = new SimpleSpawner(template);
		sp.setLoc(MAMMON_BLACKSMITH_POINTS[firstTown]);
		sp.setAmount(1);
		sp.setRespawnDelay(0);
		BlacksmithNpc = sp.doSpawn(true);
	}
	
	/**
	 * @author Mobius
	 */
	public static class TeleportMammons implements Runnable
	{
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			Functions.npcShout(BlacksmithNpc, mammonText[Rnd.get(mammonText.length)]);
			final int nextTown = Rnd.get(MAMMON_PRIEST_POINTS.length);
			PriestNpc.teleToLocation(MAMMON_PRIEST_POINTS[nextTown]);
			MerchantNpc.teleToLocation(MAMMON_MERCHANT_POINTS[nextTown]);
			BlacksmithNpc.teleToLocation(MAMMON_BLACKSMITH_POINTS[nextTown]);
		}
	}
	
	/**
	 * Method onLoad.
	 * @see l2next.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		SpawnMammons();
		_mammonTeleportTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new TeleportMammons(), PORT_TIME, PORT_TIME);
		_log.info("Loaded AI: Mammons Teleporter");
	}
	
	/**
	 * Method onReload.
	 * @see l2next.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		// empty method
	}
	
	/**
	 * Method onShutdown.
	 * @see l2next.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		if (_mammonTeleportTask != null)
		{
			_mammonTeleportTask.cancel(true);
			_mammonTeleportTask = null;
		}
	}
}
