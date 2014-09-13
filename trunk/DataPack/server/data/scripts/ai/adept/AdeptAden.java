package ai.adept;

import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.Location;

public class AdeptAden extends Adept
{
	public AdeptAden(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]{
			new Location(148344, 25704, -2038),
			new Location(146360, 25704, -2038),
			new Location(146360, 24168, -2038),
			new Location(146360, 25688, -2038)
		};
	}
}