package l2next.gameserver.network.clientpackets;

import l2next.gameserver.data.xml.holder.RecipeHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.etcitems.Recipe;
import l2next.gameserver.network.serverpackets.RecipeItemMakeInfo;

public class RequestRecipeItemMakeInfo extends L2GameClientPacket
{
	private int _id;

	/**
	 * packet type id 0xB7 format: cd
	 */
	@Override
	protected void readImpl()
	{
		_id = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		Recipe recipeList = RecipeHolder.getInstance().getRecipeById(_id);
		if(recipeList == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		sendPacket(new RecipeItemMakeInfo(activeChar, recipeList, 0xffffffff));
	}
}