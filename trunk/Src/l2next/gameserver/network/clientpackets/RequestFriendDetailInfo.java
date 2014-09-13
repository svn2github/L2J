package l2next.gameserver.network.clientpackets;

import l2next.gameserver.dao.CharacterDAO;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ExFriendDetailInfo;

/**
 * @author Darvin
 */
public final class RequestFriendDetailInfo extends L2GameClientPacket
{
	private static final String _C__D0_97_REQUESTFRIENDDETAILINFO = "[C] D0:97 RequestFriendDetailInfo";

	private String _name;

	@Override
	protected void readImpl()
	{
		_name = readS();
	}

	@Override
	public void runImpl()
	{
		Player player = getClient().getActiveChar();
		int objId = CharacterDAO.getInstance().getObjectIdByName(_name);
		player.sendPacket(new ExFriendDetailInfo());

	}

	@Override
	public String getType()
	{
		return _C__D0_97_REQUESTFRIENDDETAILINFO;
	}
}
