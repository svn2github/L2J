package l2next.gameserver.network.serverpackets;

public class ExBR_NewIConCashBtnWnd extends L2GameServerPacket
{
	private int _read;
	
	public ExBR_NewIConCashBtnWnd(int read)
	{
		_read = read;
	}
	
	@Override
	protected void writeImpl()
	{
		writeH(_read);
	}
}
