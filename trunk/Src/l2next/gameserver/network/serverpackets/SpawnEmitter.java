package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;

/**
 * Этот пакет отвечает за анимацию высасывания душ из трупов
 *
 * @author SYS
 */
public class SpawnEmitter extends L2GameServerPacket
{
	private int _monsterObjId;
	private int _playerObjId;

	public SpawnEmitter(NpcInstance monster, Player player)
	{
		_playerObjId = player.getObjectId();
		_monsterObjId = monster.getObjectId();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_monsterObjId);
		writeD(_playerObjId);
		writeD(0x00); // unk
	}
}