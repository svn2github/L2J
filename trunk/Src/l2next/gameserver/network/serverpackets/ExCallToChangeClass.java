package l2next.gameserver.network.serverpackets;

/**
 * @author Darvin
 * @data 08.06.2012
 */
public class ExCallToChangeClass extends L2GameServerPacket
{
	private int classId;
	private boolean showMsg;

	public ExCallToChangeClass(int classId, boolean showMsg)
	{
		this.classId = classId;
		this.showMsg = showMsg;
	}

	@Override
	protected void writeImpl()
	{
		writeD(classId); // New Class Id
		writeD(showMsg); // Show Message
	}
}
