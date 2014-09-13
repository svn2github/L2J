package l2next.gameserver.network.clientpackets;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.PetInstance;

public class RequestChangePetName extends L2GameClientPacket
{
	private String _name;

	@Override
	protected void readImpl()
	{
		_name = readS();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		PetInstance pet = activeChar.getFirstPet() != null && activeChar.getFirstPet().isPet() ? (PetInstance) activeChar.getFirstPet() : null;
		if(pet == null)
		{
			return;
		}

		if(pet.isDefaultName())
		{
			if(_name.length() < 1 || _name.length() > 8)
			{
				sendPacket(Msg.YOUR_PETS_NAME_CAN_BE_UP_TO_8_CHARACTERS);
				return;
			}
			pet.setName(_name);
			pet.broadcastCharInfo();
			pet.updateControlItem();
		}
	}
}