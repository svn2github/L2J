package l2next.gameserver.model.instances;

import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.templates.npc.NpcTemplate;

public class ChestInstance extends MonsterInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 2516211559188406776L;

	public ChestInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	public void tryOpen(Player opener, Skill skill)
	{
		getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, opener, 100);
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}
}