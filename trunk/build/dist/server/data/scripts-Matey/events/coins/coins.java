package events.coins;

import l2next.gameserver.Announcements;
import l2next.gameserver.listener.actor.OnDeathListener;
import l2next.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.actor.listener.CharListenerList;
import l2next.gameserver.model.instances.MonsterInstance;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class coins extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	private static long MOUSE_COIN_CHANCE = 100;
	private static int MOUSE_COIN = 10639;
	private static int MOUSE_COIN_COUNT = 4;
	private static final Logger _log = LoggerFactory.getLogger(coins.class);

	private static boolean _active = false;

	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		if(isActive())
		{
			_active = true;
			_log.info("Loaded Event: L2Coins [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: L2Coins [state: deactivated]");
		}
	}

	@Override
	public void onReload()
	{

	}

	@Override
	public void onShutdown()
	{

	}

	/**
	 * Читает статус эвента из базы.
	 *
	 * @return
	 */
	private static boolean isActive()
	{
		return IsActive("L2Coins");
	}

	/**
	 * Запускает эвент
	 */
	public void startEvent()
	{
		Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
		{
			return;
		}

		if(SetActive("L2Coins", true))
		{
			System.out.println("Event 'L2Coins' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.coins.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'L2Coins' already started.");
		}

		_active = true;

		show("admin/events/events.htm", player);
	}

	/**
	 * Останавливает эвент
	 */
	public void stopEvent()
	{
		Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
		{
			return;
		}

		if(SetActive("L2Coins", false))
		{
			System.out.println("Event 'L2Coins' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.coins.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'L2Coins' not started.");
		}

		_active = false;

		show("admin/events/events.htm", player);
	}

	@Override
	public void onPlayerEnter(Player player)
	{
		if(_active)
		{
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.coins.AnnounceEventStarted", null);
		}
	}

	@Override
	public void onDeath(Creature cha, Creature killer)
	{
		if(_active && SimpleCheckDrop(cha, killer))
		{
			long count = Util.rollDrop(1, MOUSE_COIN_COUNT, MOUSE_COIN_CHANCE * killer.getPlayer().getRateItems() * ((MonsterInstance) cha).getTemplate().rateHp * 10000L, true);
			if(count > 0)
			{
				addItem(killer.getPlayer(), MOUSE_COIN, count);
			}
		}
	}
}