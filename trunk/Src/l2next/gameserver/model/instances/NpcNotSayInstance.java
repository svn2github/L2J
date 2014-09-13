package l2next.gameserver.model.instances;

import l2next.gameserver.templates.npc.NpcTemplate;

public class NpcNotSayInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 6897784736529111053L;

	public NpcNotSayInstance(final int objectID, final NpcTemplate template)
	{
		super(objectID, template);
		setHasChatWindow(false);
	}
}
