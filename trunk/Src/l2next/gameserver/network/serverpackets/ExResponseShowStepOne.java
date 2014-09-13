package l2next.gameserver.network.serverpackets;

import l2next.gameserver.data.xml.holder.PetitionGroupHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.petition.PetitionMainGroup;
import l2next.gameserver.utils.Language;

import java.util.Collection;

/**
 * @author VISTALL
 */
public class ExResponseShowStepOne extends L2GameServerPacket
{
	private Language _language;

	public ExResponseShowStepOne(Player player)
	{
		_language = player.getLanguage();
	}

	@Override
	protected void writeImpl()
	{
		Collection<PetitionMainGroup> petitionGroups = PetitionGroupHolder.getInstance().getPetitionGroups();
		writeD(petitionGroups.size());
		for(PetitionMainGroup group : petitionGroups)
		{
			writeC(group.getId());
			writeS(group.getName(_language));
		}
	}
}