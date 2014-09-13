package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Skill;
import l2next.gameserver.model.SkillLearn;
import l2next.gameserver.tables.SkillTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ALF
 */
public class ExAcquireSkillInfo extends L2GameServerPacket
{
	private SkillLearn _learn;
	private List<Require> _reqs = new ArrayList<Require>(1);
	private List<Integer> _dels = new ArrayList<Integer>(10);
	private Skill _skill;

	public ExAcquireSkillInfo(SkillLearn learn)
	{
		_learn = learn;
		if(_learn.getItemId() != 0)
		{
			_reqs.add(new Require(_learn.getItemId(), _learn.getItemCount()));
		}

		// Разницы в ЛвЛе нету, так как список удаляемых скилов не зависит от
		// лвла скила
		// 1 - самый оптимиальный вариант так как нету необходиомсти
		// конвертирования
		// лвла скила с клиентского представления в серверное.
		_skill = SkillTable.getInstance().getInfo(learn.getId(), 1);

		// Не раельно, но всё же....

	}

	@Override
	protected void writeImpl()
	{
		writeD(_learn.getId());
		writeD(_learn.getLevel());
		writeD(_learn.getCost());
		writeH(_learn.getMinLevel());
		writeH(0);  // Tauti
		writeD(_reqs.size());
		for(Require temp : _reqs)
		{
			writeD(temp.itemId);
			writeQ(temp.count);
		}
		Skill relskill = SkillTable.getInstance().getInfo(_learn.getId(), _learn.getLevel());
		if(relskill.isRelationSkill())
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

	private static class Require
	{

		public int itemId;
		public long count;

		public Require(int pItemId, long pCount)
		{
			itemId = pItemId;
			count = pCount;
		}
	}
}
