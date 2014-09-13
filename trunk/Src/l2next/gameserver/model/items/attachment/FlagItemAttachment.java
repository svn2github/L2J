package l2next.gameserver.model.items.attachment;

import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;

/**
 * @author VISTALL
 * @date 15:49/26.03.2011
 */
public interface FlagItemAttachment extends PickableAttachment
{
	// FIXME [VISTALL] возможно переделать на слушатели игрока
	void onLogout(Player player);

	// FIXME [VISTALL] возможно переделать на слушатели игрока
	void onDeath(Player owner, Creature killer);

	boolean canAttack(Player player);

	boolean canCast(Player player, Skill skill);
}
