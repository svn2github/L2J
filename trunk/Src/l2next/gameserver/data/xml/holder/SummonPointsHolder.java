package l2next.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntIntHashMap;
import l2next.commons.data.xml.AbstractHolder;

/**
 * @author ALF
 * @date 09.08.2012
 */
public class SummonPointsHolder extends AbstractHolder
{
	private static SummonPointsHolder _instance = new SummonPointsHolder();

	private static final TIntIntHashMap _summonPoints = new TIntIntHashMap();

	public static SummonPointsHolder getInstance()
	{
		return _instance;
	}

	public void addSummonPoints(int npcId, int point)
	{
		_summonPoints.put(npcId, point);
	}

	public int getPointsForSummonId(int npcId)
	{
		return _summonPoints.get(npcId);
	}

	@Override
	public int size()
	{
		return _summonPoints.size();
	}

	@Override
	public void clear()
	{
		_summonPoints.clear();
	}

}
