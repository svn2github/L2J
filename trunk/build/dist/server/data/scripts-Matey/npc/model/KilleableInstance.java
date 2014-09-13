package npc.model;

import l2next.commons.util.Rnd;
import l2next.gameserver.GameTimeController;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * @author Awakeninger
 * @date 26.08.2013
 * Обычный класс используемый для "убиваемых" нпц
 */
public class KilleableInstance extends NpcInstance
{
	public KilleableInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public boolean isInvul()
	{
		return false;
	}
}