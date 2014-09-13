package npc.model.residences.clanhall;

import l2next.gameserver.model.entity.residence.Residence;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * @author VISTALL
 * @date 13:19/31.03.2011
 */
public class DoormanInstance extends npc.model.residences.DoormanInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7919187481215134350L;

	public DoormanInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public int getOpenPriv()
	{
		return Clan.CP_CH_ENTRY_EXIT;
	}

	@Override
	public Residence getResidence()
	{
		return getClanHall();
	}
}
