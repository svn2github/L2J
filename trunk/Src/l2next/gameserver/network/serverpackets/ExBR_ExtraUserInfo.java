package l2next.gameserver.network.serverpackets;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Player;
import l2next.gameserver.skills.AbnormalEffect;

public class ExBR_ExtraUserInfo extends L2GameServerPacket
{
	private int _objectId;
	private GArray<AbnormalEffect> _effect3;

	public ExBR_ExtraUserInfo(Player cha)
	{
		_objectId = cha.getObjectId();
		_effect3 = cha.getAbnormalEffects();
	}

	@Override
	protected void writeImpl()
	{
		writeD(_objectId); // object id of player
		writeD(_effect3.size()); // event effect id
		for(AbnormalEffect ae : _effect3)
		{
			writeD(ae.getMask());
		}
		writeC(0);
	}
}