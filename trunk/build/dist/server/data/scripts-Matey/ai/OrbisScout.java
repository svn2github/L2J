package ai;

import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.spawn.WalkerRouteTemplate;
import l2next.gameserver.templates.spawn.WalkerRouteTemplate.Route;
import l2next.gameserver.templates.spawn.WalkerRouteTemplate.RouteType;
import l2next.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KilRoy РђР� РґР»СЏ СЃРёСЃС‚РµРјС‹ С…РѕР¶РґРµРЅРёСЏ,Р±СЂРѕР¶РµРЅРёСЏ,РґСЂРѕС‡РµРІР°РЅРёСЏ Р±РµС€РµРЅС‹С… РЅРµРїРёСЃРµР№. Р”СѓРјР°СЋ РїР°СЂСѓ С‚РёРїРѕРІ РґР»СЏ РЅР°С‡Р°Р»Р° РІРїРёР»РёРј, РїРѕС‚РѕРј РїРѕСЃРјРѕС‚СЂРёРј РЅР° СЂР°Р·РІРёС‚РёРµ СЃРѕР±С‹С‚РёР№
 *         :) LINEAR - Р›РёРЅРµР№РЅРѕР№Рµ С…РѕР¶РґРµРЅРёРµ (РѕС‚ РїРµСЂРІРѕР№ Рє РїРѕСЃР». Рё СЃ РїРѕСЃР». РґРѕ РїРµСЂРІРѕР№) CYCLE - Р¦РёРєР»РёС‡РЅРѕСЃС‚СЊ, СЃР°РјРѕ СЃРѕР±РѕР№ (РѕС‚ РїРµСЂРІРѕР№ Рє РїРѕСЃР». Рё СЃ РїРѕСЃР». РїРѕРёСЃРє
 *         РїРµСЂРІРѕР№) TELEPORT - РўРџ РїСЂРё СЂРѕСѓС‚Рµ Рє РїРѕСЃР». С‚РѕС‡РєРµ
 *
 *         Р�РґРµСЏ - Youri
 */
public class OrbisScout extends Fighter
{
	private static final Logger _log = LoggerFactory.getLogger(OrbisScout.class);
	private int _routeIndex = 0;
	private short _direction = 1;
	private long _lastMove = 0;
	private Creature player;
	private NpcInstance actor;
	//private Location _spawnedLoc = new Location();
	private int current_point = -1;
	//private Location loc = actor.getSpawnedLoc();
	private Player character;

	public OrbisScout(NpcInstance actor)
	{
		super(actor);
		setIntention(CtrlIntention.AI_INTENTION_ACTIVE);

	}

	@Override
	protected boolean thinkActive()
	{
		WalkerRouteTemplate routeTemplate = getActor().getWalkerRouteTemplate();

		if(routeTemplate == null)
		{
			return false;
		}

		if(character != null && player.isInRange(actor, 300))
		{
			_log.info("ATTACK");
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, player, 1);
			return false;
		}

		//if (player.isPlayer() && player.isInRange(loc, 300))
		//{Р—Р°Р±С‹Р» СѓРїРѕРјСЏРЅСѓС‚СЊ, С‡С‚Рѕ РґРѕРЅ РІ РєСЂС‹СЃСѓ СЃР»РёРІР°РµС‚ С‡СѓР¶РѕРµ Рё РїСЂРѕРґР°С‘С‚ С‡СѓР¶РѕРµ) 
		//Р”СѓРјР°СЋ Р±СѓРґРµС‚ РєР°Рє СЃ Р“РѕР”РѕРј :D 3 РёСЃС…РѕРґРЅРёРєРѕРІ Рё С„РµР№Р» :D

		//	_log.info("ATTACK");
		//	changeIntention(CtrlIntention.AI_INTENTION_ATTACK, player, null);
		//	//setIntention(CtrlIntention.AI_INTENTION_ATTACK);
		//	}

		boolean LINEAR = (routeTemplate.getRouteType() == RouteType.LINEAR);

		if(routeTemplate.getIsRunning())
		{
			getActor().setRunning();
		}

		int pointsCount = routeTemplate.getPointsCount();
		if(pointsCount <= 0)
		{
			return false;
		}

		Route point = null;
		int oldIndex = _routeIndex;

		if((_routeIndex + _direction) >= pointsCount || (_routeIndex + _direction) < 0)
		{
			if(LINEAR)
			{
				_direction *= -1;
				_routeIndex += _direction;
				point = routeTemplate.getPoints().get(_routeIndex);
			}
		}
		else
		{
			_routeIndex += _direction;
			point = routeTemplate.getPoints().get(_routeIndex);
		}

		Location nextLoc = point.getLoc();
		long delay = (point.getDelay() <= 0) ? routeTemplate.getDelay() : point.getDelay();

		if(_lastMove == 0)
		{
			_lastMove = System.currentTimeMillis() + delay;
			_routeIndex = oldIndex;
			return false;
		}
		else if(getActor().isMoving)
		{
			_routeIndex = oldIndex;
			return false;
		}
		else if(System.currentTimeMillis() - _lastMove > delay)
		{
			getActor().moveToLocation(nextLoc, 0, true);
			_lastMove = System.currentTimeMillis();
		}

		return true;
	}

	@Override
	protected boolean createNewTask()
	{
		return defaultFightTask();
	}

	@Override
	public boolean checkAggression(Creature target)
	{
		return super.checkAggression(target);
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected void thinkAttack()
	{

		checkAggression(player);

		super.thinkAttack();
	}
}