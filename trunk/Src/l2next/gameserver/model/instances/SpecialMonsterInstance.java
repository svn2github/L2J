package l2next.gameserver.model.instances;

import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * Это алиас L2MonsterInstance используемый для монстров, у которых нестандартные статы
 */
public class SpecialMonsterInstance extends MonsterInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = -3880533257608561352L;

	public SpecialMonsterInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}
}