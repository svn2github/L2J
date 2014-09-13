package l2next.gameserver.network.clientpackets;

import l2next.commons.lang.ArrayUtils;
import l2next.gameserver.data.xml.holder.SkillAcquireHolder;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.SkillLearn;
import l2next.gameserver.model.base.AcquireType;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.AcquireSkillInfo;
import l2next.gameserver.network.serverpackets.ExAcquireSkillInfo;
import l2next.gameserver.tables.SkillTable;

/**
 * Reworked: VISTALL
 */
public class RequestAquireSkillInfo extends L2GameClientPacket
{
	private int _id;
	private int _level;
	private AcquireType _type;

	@Override
	protected void readImpl()
	{
		_id = readD();
		_level = readD();
		_type = ArrayUtils.valid(AcquireType.VALUES, readD());
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null || player.getTransformation() != 0 || SkillTable.getInstance().getInfo(_id, _level) == null || _type == null)
		{
			return;
		}

		NpcInstance trainer = player.getLastNpc();
		if((trainer == null || player.getDistance(trainer.getX(), trainer.getY()) > Creature.INTERACTION_DISTANCE) && !player.isGM() && _type != AcquireType.NORMAL)
		{
			return;
		}

		SkillLearn skillLearn = SkillAcquireHolder.getInstance().getSkillLearn(player, _id, _level, _type);
		if(skillLearn == null)
		{
			return;
		}
		if(_type == AcquireType.NORMAL)
		{
			sendPacket(new ExAcquireSkillInfo(skillLearn));
		}
		else
		{
			sendPacket(new AcquireSkillInfo(_type, skillLearn));
		}
	}
}