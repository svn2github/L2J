package l2next.gameserver.instancemanager.itemauction;

import l2next.commons.lang.ArrayUtils;

public enum ItemAuctionState
{
	CREATED, STARTED, FINISHED;

	public static final ItemAuctionState stateForStateId(int stateId)
	{
		return ArrayUtils.valid(values(), stateId);
	}
}