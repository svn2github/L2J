package npc.model;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.MerchantInstance;
import l2next.gameserver.network.serverpackets.ExGetPremiumItemList;
import l2next.gameserver.network.serverpackets.PackageToList;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.WarehouseFunctions;

/**
 * @author VISTALL
 * @date 20:32/16.05.2011
 */
public class FreightSenderInstance extends MerchantInstance
{
	/**
	 *
	 */

	private static final long serialVersionUID = -2189525902583757257L;

	public FreightSenderInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}

		if(command.equalsIgnoreCase("deposit_items"))
		{
			player.sendPacket(new PackageToList(player));
		}
		else if(command.equalsIgnoreCase("withdraw_items"))
		{
			WarehouseFunctions.showFreightWindow(player);
		}
		else if(command.equalsIgnoreCase("ReceivePremium"))
		{
			if(player.getPremiumItemList().isEmpty())
			{
				player.sendPacket(Msg.THERE_ARE_NO_MORE_VITAMIN_ITEMS_TO_BE_FOUND);
				return;
			}

			player.sendPacket(new ExGetPremiumItemList(player));
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}

