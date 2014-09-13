package l2next.gameserver.model.quest.dynamic;

import l2next.gameserver.model.Player;

public interface ICheckStartCondition
{
	public boolean checkCondition(Player player);
}