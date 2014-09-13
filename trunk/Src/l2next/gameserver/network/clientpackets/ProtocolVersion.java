package l2next.gameserver.network.clientpackets;

import l2next.gameserver.Config;
import l2next.gameserver.network.GameClient;
import l2next.gameserver.network.serverpackets.VersionCheck;
import l2next.gameserver.network.serverpackets.SendStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * packet type id 0x0E format: cdbd
 */
public class ProtocolVersion extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(ProtocolVersion.class);
	private int _version;
	private byte[] _check;
	private byte[] _data;
	private String _hwidHdd = "", _hwidMac = "", _hwidCPU = "";

	@Override
	protected void readImpl()
	{
		GameClient client = getClient();
		_version = readD();
	}

	@Override
	protected void runImpl()
	{
		if(_version == -2)
		{
			_client.closeNow(false);
			return;
		}
		else if(_version == -3)
		{
			_log.info("Status request from IP : " + getClient().getIpAddr());
			getClient().close(new SendStatus());
			return;
		}
		else if(_version < Config.MIN_PROTOCOL_REVISION || _version > Config.MAX_PROTOCOL_REVISION)
		{
			_log.warn("Unknown protocol revision : " + _version + ", client : " + _client);
			getClient().close(new VersionCheck(null));
			return;
		}

		getClient().setRevision(_version);
		sendPacket(new VersionCheck(_client.enableCrypt()));
	}

	public String getType()
	{
		return getClass().getSimpleName();
	}
}