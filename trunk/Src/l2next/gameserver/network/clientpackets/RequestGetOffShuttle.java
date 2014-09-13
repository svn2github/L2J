package l2next.gameserver.network.clientpackets;

import l2next.gameserver.data.BoatHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.boat.Boat;
import l2next.gameserver.utils.Location;

/**
 * @author Bonux
 */
public class RequestGetOffShuttle extends L2GameClientPacket
{
	private int _shuttleId;
	private Location _location = new Location();

	@Override
	protected void readImpl()
	{
		_shuttleId = readD();
		_location.x = readD();
		_location.y = readD();
		_location.z = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		Boat boat = BoatHolder.getInstance().getBoat(_shuttleId);
		if(boat == null || boat.isMoving)
		{
			player.sendActionFailed();
			return;
		}

		boat.oustPlayer(player, _location, false);
	}
}