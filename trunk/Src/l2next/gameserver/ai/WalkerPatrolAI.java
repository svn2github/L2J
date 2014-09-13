package l2next.gameserver.ai;

import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.spawn.WalkerAgroTemplate;
import l2next.gameserver.templates.spawn.WalkerAgroTemplate.Route;
import l2next.gameserver.templates.spawn.WalkerAgroTemplate.RouteType;
import l2next.gameserver.utils.Location;

/**
 * @author Awakeninger 
 * AI для "патрульных" агрессивных мобов, типа "Разведчик Орбиса"
 */
public class WalkerPatrolAI extends DefaultAI
{
	private int _routeIndex = 0;
	private short _direction = 1;
	private long _lastMove = 0;
	private NpcInstance actor;

	public WalkerPatrolAI(NpcInstance actor)
	{
		super(actor);
		setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
	}

	@Override
	protected boolean thinkActive()
	{
		WalkerAgroTemplate routeTemplate = getActor().getWalkerAgroTemplate();

		if(routeTemplate == null)
		{
			return false;
		}

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
		if(!target.isPlayer())
		{
			return false;
		}
		return super.checkAggression(target);
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

}