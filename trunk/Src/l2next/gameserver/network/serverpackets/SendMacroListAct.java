package l2next.gameserver.network.serverpackets;

/**
 * packet type id 0xe7
 * <p/>
 * sample
 * <p/>
 * e7 d // unknown change of Macro edit,add,delete c // unknown c //count of Macros c // unknown
 * <p/>
 * d // id S // macro name S // desc S // acronym c // icon c // count
 * <p/>
 * c // entry c // type d // skill id c // shortcut id S // command name
 * <p/>
 * format: cdccdSSScc (ccdcS)
 */
public class SendMacroListAct extends L2GameServerPacket
{
	@Override
	protected final void writeImpl()
	{
	}
}