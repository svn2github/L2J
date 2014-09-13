package l2next.gameserver.taskmanager.actionrunner.tasks;

import l2next.gameserver.instancemanager.commission.CommissionShopManager;

/**
 * @author Camelion
 * @date 22.04.12  13:05
 */
public class CommissionShopExpiredItemsTask extends AutomaticTask
{
	@Override
	public void doTask() throws Exception
	{
		CommissionShopManager.getInstance().returnExpiredItems();
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return System.currentTimeMillis() + 600000L;
	}
}