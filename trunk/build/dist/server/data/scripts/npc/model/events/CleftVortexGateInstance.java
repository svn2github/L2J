package npc.model.events;

import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * @author VISTALL
 * @date 21:09/15.07.2011
 */
public class CleftVortexGateInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 9094154840784357828L;

	public CleftVortexGateInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setShowName(false);
	}
}
