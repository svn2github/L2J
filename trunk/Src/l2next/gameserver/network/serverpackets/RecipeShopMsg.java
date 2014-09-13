package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;

public class RecipeShopMsg extends L2GameServerPacket
{
	private int _objectId;
	private String _storeName;

	public RecipeShopMsg(Player player)
	{
		_objectId = player.getObjectId();
		_storeName = player.getManufactureName();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_objectId);
		writeS(_storeName);
	}
}