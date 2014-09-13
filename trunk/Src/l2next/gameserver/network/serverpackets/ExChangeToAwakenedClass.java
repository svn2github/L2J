package l2next.gameserver.network.serverpackets;

/**
 * @author Darvin
 * @data 08.06.2012
 *       <p/>
 *       Отображает окно для смены класса на перерожденный
 */
public class ExChangeToAwakenedClass extends L2GameServerPacket
{
	private int classId;

	public ExChangeToAwakenedClass(int classId)
	{
		this.classId = classId;
	}

	@Override
	protected void writeImpl()
	{
		writeD(classId);
	}
}
