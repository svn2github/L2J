package l2next.gameserver.network.serverpackets;

import l2next.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author VISTALL
 * @date 23/03/2011
 */
public class ConfirmDlg extends SysMsgContainer<ConfirmDlg>
{
	private int _time;
	private int _requestId;

	public ConfirmDlg(SystemMsg msg, int time)
	{
		super(msg);
		_time = time;
	}

	@Override
	protected final void writeImpl()
	{
		writeElements();
		writeD(_time);
		writeD(_requestId);
	}

	public void setRequestId(int requestId)
	{
		_requestId = requestId;
	}
}