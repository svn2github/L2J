package l2next.gameserver.network.serverpackets.UnionPledge;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPledgeUnionStateInfo extends L2GameServerPacket
{
	private int _unionType;
	private int _collectNum;
	private int _successNum;

	public ExPledgeUnionStateInfo(int unionType, int collectNum, int successNum)
	{
		_unionType = unionType;
		_collectNum = collectNum;
		_successNum = successNum;
	}
	
	@Override
	protected void writeImpl()
	{
		writeD(_unionType);
		writeD(_collectNum);
		writeD(_successNum);
	}
}