package npc.model;

import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.MonsterInstance;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.templates.npc.NpcTemplate;

public class SeducedInvestigatorInstance extends MonsterInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = -7648445176450992792L;

	public SeducedInvestigatorInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setHasChatWindow(true);
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		player.sendPacket(new NpcHtmlMessage(player, this, "common/seducedinvestigator.htm", val));
	}

	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		Player player = attacker.getPlayer();
		if(player == null)
		{
			return false;
		}
		if(player.isPlayable())
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean isMovementDisabled()
	{
		return true;
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}
}