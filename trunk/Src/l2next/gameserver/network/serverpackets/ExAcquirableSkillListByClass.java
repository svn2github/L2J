package l2next.gameserver.network.serverpackets;

import l2next.gameserver.data.xml.holder.SkillAcquireHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.SkillLearn;
import l2next.gameserver.tables.SkillTable;

import java.util.Collection;

/**
 * @author ALF
 * @date 17.07.2012
 */
public class ExAcquirableSkillListByClass extends L2GameServerPacket
{

	private final Collection<SkillLearn> allskills;

	public ExAcquirableSkillListByClass(Player player)
	{
		allskills = SkillAcquireHolder.getInstance().getAvailableAllSkills(player);
	}

	@Override
	protected final void writeImpl()
	{
		writeD(allskills.size());
		for(SkillLearn sk : allskills)
		{
			Skill skill = SkillTable.getInstance().getInfo(sk.getId(), sk.getLevel());

			if(skill == null)
			{
				continue;
			}

			writeD(sk.getId());
			writeD(sk.getLevel());
			writeD(sk.getCost());
			writeH(sk.getMinLevel());
			writeH(0x00); // Tauti
			boolean consumeItem = sk.getItemId() > 0;
			writeD(consumeItem ? 1 : 0);
			if(consumeItem)
			{
				writeD(sk.getItemId());
				writeQ(sk.getItemCount());
			}
			Skill relskill = SkillTable.getInstance().getInfo(sk.getId(), sk.getLevel());
			if(relskill != null && relskill.isRelationSkill())
			{
				int[] _dels = relskill.getRelationSkills();
				writeD(_dels.length);// deletedSkillsSize
				for(int skillId : _dels)
				{
					writeD(skillId);// skillId
					writeD(SkillTable.getInstance().getBaseLevel(skillId));// skillLvl
				}
			}
			else
			{
				writeD(0x00);
			}
		}
	}
}
