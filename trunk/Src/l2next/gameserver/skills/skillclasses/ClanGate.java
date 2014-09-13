package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.templates.StatsSet;

public class ClanGate extends Skill
{
	public ClanGate(StatsSet set)
	{
		super(set);
	}

	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if(!activeChar.isPlayer())
		{
			return false;
		}

		Player player = (Player) activeChar;
		if(!player.isClanLeader())
		{
			player.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
			return false;
		}

		SystemMessage msg = Call.canSummonHere(player);
		if(msg != null)
		{
			activeChar.sendPacket(msg);
			return false;
		}

		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		if(!activeChar.isPlayer())
		{
			return;
		}

		Player player = (Player) activeChar;
		Clan clan = player.getClan();
		clan.broadcastToOtherOnlineMembers(Msg.COURT_MAGICIAN__THE_PORTAL_HAS_BEEN_CREATED, player);

		getEffects(activeChar, activeChar, getActivateRate() > 0, true);
	}
}
