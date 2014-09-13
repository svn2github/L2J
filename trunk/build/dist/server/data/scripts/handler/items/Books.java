package handler.items;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.network.serverpackets.SSQStatus;
import l2next.gameserver.network.serverpackets.ShowXMasSeal;
import l2next.gameserver.network.serverpackets.TutorialShowHtml;

public class Books extends SimpleItemHandler
{
	private static final int[] ITEM_IDS = new int[]{
		5555,
		5707,
		32777,
		32778
	};

	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}

	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		int itemId = item.getItemId();

		NpcHtmlMessage htmlMessage = new NpcHtmlMessage(17819);
		htmlMessage.setFile("quests\17725.htm");
		switch(itemId)
		{
			case 5555:
				player.sendPacket(new ShowXMasSeal(5555));
				break;
			case 5707:
				player.sendPacket(new SSQStatus(player, 1));
				break;
			case 32777:
				player.sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_ADVENTURER, TutorialShowHtml.TYPE_WINDOW));
				break;
			case 32778:
				player.sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_AWAKING, TutorialShowHtml.TYPE_WINDOW));
				break;
			default:
				return false;
		}

		return true;
	}
}
