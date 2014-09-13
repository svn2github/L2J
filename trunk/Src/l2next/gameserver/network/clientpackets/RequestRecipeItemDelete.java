package l2next.gameserver.network.clientpackets;

import l2next.gameserver.dao.CharacterRecipebookDAO;
import l2next.gameserver.data.xml.holder.RecipeHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.etcitems.Recipe;
import l2next.gameserver.network.serverpackets.RecipeBookItemList;

public class RequestRecipeItemDelete extends L2GameClientPacket
{
	private int _recipeId;

	@Override
	protected void readImpl()
	{
		_recipeId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		if(activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_MANUFACTURE)
		{
			activeChar.sendActionFailed();
			return;
		}

		Recipe rp = RecipeHolder.getInstance().getRecipeById(_recipeId);
		if(rp == null || !activeChar.findRecipe(_recipeId))
		{
			activeChar.sendActionFailed();
			return;
		}

		CharacterRecipebookDAO.getInstance().unregisterRecipe(activeChar.getObjectId(), _recipeId);
		if(activeChar.getDwarvenRecipeBook().contains(rp))
		{
			activeChar.removeDwarvenRecipe(_recipeId);
		}
		else
		{
			activeChar.removeCommonRecipe(_recipeId);
		}

		activeChar.sendPacket(new RecipeBookItemList(activeChar, !rp.isCommon()));
	}
}