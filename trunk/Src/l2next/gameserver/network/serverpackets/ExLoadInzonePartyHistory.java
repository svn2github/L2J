package l2next.gameserver.network.serverpackets;

import java.util.Iterator;
import java.util.List;

/**
* @Author Awakeninger
* @TODO[A]: Расписал позиции, надо доделать
*/
public class ExLoadInzonePartyHistory extends L2GameServerPacket
{
	private int _charId;
	private int partySize = 1;
	private int partyId = 1;
	private String name = new String("Awakeninger");
	private int instanceId = 1;
	private int instanceTime = 1;
	private int classId = 1;

	public ExLoadInzonePartyHistory(int charId)
	{
		_charId = charId;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(partySize);// Кол-во пати. 1 для дальнейшего
		if (partySize > 0)
		{
			//TODO:for(1 1 : 1);
			writeD(partyId);
			writeD(instanceId);
			writeD(instanceTime);
			writeS(name);
			writeH(classId);
			writeH(1); //Вот тут хз что
		}
    }
}