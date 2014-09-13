package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Creature;

public class NickNameChanged extends L2GameServerPacket
{
	private final int objectId;
	private final String title;

	public NickNameChanged(Creature cha)
	{
		objectId = cha.getObjectId();
		title = cha.getTitle();
	}

	@Override
	protected void writeImpl()
	{
		writeD(objectId);
		writeS(title);
	}
}