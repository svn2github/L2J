package l2next.gameserver.ai;

import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.spawn.WalkerRouteTemplate;
import l2next.gameserver.templates.spawn.WalkerRouteTemplate.Route;
import l2next.gameserver.templates.spawn.WalkerRouteTemplate.RouteType;
import l2next.gameserver.utils.Location;

/**
 * @author KilRoy АИ для системы хождения,брожения,дрочевания бешеных неписей. Думаю пару типов для начала впилим, потом посмотрим на развитие событий
 *         :) LINEAR - Линейнойе хождение (от первой к посл. и с посл. до первой) CYCLE - Цикличность, само собой (от первой к посл. и с посл. поиск
 *         первой) TELEPORT - ТП при роуте к посл. точке
 *
 *         Идея - Youri
 */
public class WalkerAI extends DefaultAI
{
	private int _routeIndex = 0;
	private short _direction = 1;
	private long _lastMove = 0;
	private NpcInstance actor;

	public WalkerAI(NpcInstance actor)
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

		boolean LINEAR = (routeTemplate.getRouteType() == RouteType.LINEAR);
		boolean CYCLE = (routeTemplate.getRouteType() == RouteType.CYCLE);
		boolean TELEPORT = (routeTemplate.getRouteType() == RouteType.TELEPORT);

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
			else if(CYCLE)
			{
				_direction = 1;
				_routeIndex = 0;
				point = routeTemplate.getPoints().get(_routeIndex);
			}
			else if(TELEPORT)
			{
				_direction = 1;
				_routeIndex = 0;
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
			if(TELEPORT & point.getLastPoint())
			{
				getActor().teleToLocation(nextLoc);
				_lastMove = System.currentTimeMillis();
			}
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

}