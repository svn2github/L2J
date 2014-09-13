package l2next.gameserver.model.entity.olympiad;

import l2next.commons.configuration.ExProperties;
import l2next.gameserver.Config;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.dao.OlympiadNobleDAO;
import l2next.gameserver.instancemanager.OlympiadHistoryManager;
import l2next.gameserver.instancemanager.ServerVariables;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Summon;
import l2next.gameserver.model.entity.Hero;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.CustomMessage;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.templates.StatsSet;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.MultiValueIntegerMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

public class Festival
{
	private static ChaosFestivalStatus _festivalStatus;
	private static final int GRANKAIN_LUMIERE = 33685;
	private static final int[] ARENAS = {220,221,222,223};
	private static boolean isPvP;
	private static int currentArena;
	private static List<Player> challenger;
	private static Player player;
	
	public static void init()
	{
		challenger = new ArrayList<Player>();
	}
	
	public static void startChallenge(List<Player> players)
	{
		
	}
	
	public static int getArenaID()
	{
		//currentArena = ARENAS[Rnd.get(ARENAS.length)];
		return currentArena;
	}
	
	/*private void enterInstance(Player player)
	{
		Reflection r = player.getActiveReflection();
		if(player.canEnterInstance(currentArena))
		{
			ReflectionUtils.enterReflection(player, currentArena);
		}
	}*/
	
	public static boolean getType()
	{
		return isPvP;
	}
	
	public static void setType(boolean type)
	{
		isPvP = type;
	}
	
	public static ChaosFestivalStatus getStatus()
	{
		return _festivalStatus;
	}
	
	public static enum ChaosFestivalStatus
	{
		SCHEDULED, 
		INVITING, 
		PREPARING, 
		RUNNING;
	}
	
	

}