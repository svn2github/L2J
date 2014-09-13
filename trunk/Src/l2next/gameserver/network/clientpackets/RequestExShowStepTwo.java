package l2next.gameserver.network.clientpackets;

import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.PetitionGroupHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.petition.PetitionMainGroup;
import l2next.gameserver.network.serverpackets.ExResponseShowStepTwo;

/**
 * @author VISTALL
 */
public class RequestExShowStepTwo extends L2GameClientPacket
{
	private int _petitionGroupId;

	@Override
	protected void readImpl()
	{
		_petitionGroupId = readC();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null || !Config.EX_NEW_PETITION_SYSTEM)
		{
			return;
		}

		PetitionMainGroup group = PetitionGroupHolder.getInstance().getPetitionGroup(_petitionGroupId);
		if(group == null)
		{
			return;
		}

		player.setPetitionGroup(group);
		player.sendPacket(new ExResponseShowStepTwo(player, group));
	}
}