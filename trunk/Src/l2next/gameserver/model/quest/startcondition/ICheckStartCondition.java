package l2next.gameserver.model.quest.startcondition;

import l2next.gameserver.model.Player;

public interface ICheckStartCondition
{
	public boolean checkCondition(Player player);
}
