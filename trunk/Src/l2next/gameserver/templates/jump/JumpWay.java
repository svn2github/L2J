package l2next.gameserver.templates.jump;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author K1mel
 * @twitter http://twitter.com/k1mel_developer
 */
public class JumpWay
{
	private final int _id;
	private final TIntObjectHashMap<JumpPoint> _points;

	public JumpWay(int id)
	{
		_id = id;
		_points = new TIntObjectHashMap<JumpPoint>();
	}

	public int getId()
	{
		return _id;
	}

	public JumpPoint[] getPoints()
	{
		return _points.values(new JumpPoint[_points.size()]);
	}

	public JumpPoint getJumpPoint(int nextWayId)
	{
		return _points.get(nextWayId);
	}

	public void addPoint(JumpPoint point)
	{
		_points.put(point.getNextWayId(), point);
	}
}