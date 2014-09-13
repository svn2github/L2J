package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.party.PartySubstitute;

public class PartySmallWindowUpdate extends L2GameServerPacket
{
	private int obj_id, class_id, level;
	private int curCp, maxCp, curHp, maxHp, curMp, maxMp, vitality;
	private String obj_name;
	private int replace;

	public PartySmallWindowUpdate(Player member)
	{
		obj_id = member.getObjectId();
		obj_name = member.getName();
		curCp = (int) member.getCurrentCp();
		maxCp = member.getMaxCp();
		curHp = (int) member.getCurrentHp();
		maxHp = member.getMaxHp();
		curMp = (int) member.getCurrentMp();
		maxMp = member.getMaxMp();
		level = member.getLevel();
		class_id = member.getClassId().getId();
		vitality = member.getVitality().getPoints();
		replace = PartySubstitute.getInstance().isPlayerToReplace(member) ? 1 : 0;
	}

	@Override
	protected final void writeImpl()
	{
		// dSdddddddd
		writeD(obj_id);
		writeS(obj_name);
		writeD(curCp);
		writeD(maxCp);
		writeD(curHp);
		writeD(maxHp);
		writeD(curMp);
		writeD(maxMp);
		writeD(level);
		writeD(class_id);
		writeD(vitality);
		writeD(replace); // Идет ли поиск замены игроку
	}
}