package l2next.gameserver.network.clientpackets;

import l2next.commons.util.Rnd;
import l2next.gameserver.Config;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.dao.CharacterRecipebookDAO;
import l2next.gameserver.data.xml.holder.ItemHolder;
import l2next.gameserver.data.xml.holder.RecipeHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.etcitems.Recipe;
import l2next.gameserver.model.items.etcitems.RecipeIngredient;
import l2next.gameserver.model.items.etcitems.RecipeProduction;
import l2next.gameserver.network.serverpackets.ActionFail;
import l2next.gameserver.network.serverpackets.RecipeItemMakeInfo;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.templates.item.EtcItemTemplate.EtcItemType;
import l2next.gameserver.utils.ItemFunctions;

import java.util.Collection;

public class RequestRecipeItemMakeSelf extends L2GameClientPacket
{
	private int _recipeId;

	/**
	 * packet type id 0xB8 format: cd
	 */
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

		if(activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isInStoreMode())
		{
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isProcessingRequest())
		{
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}

		Recipe recipe = RecipeHolder.getInstance().getRecipeById(_recipeId);

		if(recipe == null || recipe.getIngredients().isEmpty() || recipe.getProductions().isEmpty())
		{
			activeChar.sendPacket(Msg.THE_RECIPE_IS_INCORRECT);
			return;
		}

		if(activeChar.getCurrentMp() < recipe.getMpConsume())
		{
			activeChar.sendPacket(Msg.NOT_ENOUGH_MP, new RecipeItemMakeInfo(activeChar, recipe, 0));
			return;
		}

		if(!activeChar.findRecipe(_recipeId))
		{
			activeChar.sendPacket(Msg.PLEASE_REGISTER_A_RECIPE, ActionFail.STATIC);
			return;
		}

		activeChar.getInventory().writeLock();
		try
		{
			Collection<RecipeIngredient> ingredients = recipe.getIngredients();

			for(RecipeIngredient ingredient : ingredients)
			{
				if(ingredient.getCount() == 0)
				{
					continue;
				}

				if(Config.ALT_GAME_UNREGISTER_RECIPE && ItemHolder.getInstance().getTemplate(ingredient.getItemId()).getItemType() == EtcItemType.RECIPE)
				{
					Recipe rp = RecipeHolder.getInstance().getRecipeByRecipeItem(ingredient.getItemId());
					if(activeChar.hasRecipe(rp))
					{
						continue;
					}
					activeChar.sendPacket(Msg.NOT_ENOUGH_MATERIALS, new RecipeItemMakeInfo(activeChar, recipe, 0));
					return;
				}

				ItemInstance item = activeChar.getInventory().getItemByItemId(ingredient.getItemId());
				if(item == null || item.getCount() < ingredient.getCount())
				{
					activeChar.sendPacket(Msg.NOT_ENOUGH_MATERIALS, new RecipeItemMakeInfo(activeChar, recipe, 0));
					return;
				}
			}

			for(RecipeIngredient ingredient : ingredients)
			{
				if(ingredient.getCount() != 0)
				{
					if(Config.ALT_GAME_UNREGISTER_RECIPE && ItemHolder.getInstance().getTemplate(ingredient.getItemId()).getItemType() == EtcItemType.RECIPE)
					{
						CharacterRecipebookDAO.getInstance().unregisterRecipe(activeChar.getObjectId(), RecipeHolder.getInstance().getRecipeByRecipeItem(ingredient.getItemId()).getId());
					}
					else
					{
						if(!activeChar.getInventory().destroyItemByItemId(ingredient.getItemId(), ingredient.getCount()))
						{
							continue;// TODO audit
						}
						activeChar.sendPacket(SystemMessage2.removeItems(ingredient.getItemId(), ingredient.getCount()));
					}
				}
			}
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}

		activeChar.resetWaitSitTime();
		activeChar.reduceCurrentMp(recipe.getMpConsume(), null);

		int tryCount = 1, success = 0;
		if(Rnd.chance(Config.CRAFT_DOUBLECRAFT_CHANCE))
		{
			tryCount++;
		}

		for(int i = 0; i < tryCount; i++)
		{

			if(Rnd.chance(recipe.getSuccessRate()))
			{
				for(RecipeProduction rp : recipe.getProductions())
				{
					success = 0;
					// TODO [G1ta0] добавить проверку на перевес
					if(Rnd.chance(rp.getChance()))
					{
						ItemFunctions.addItem(activeChar, rp.getItemId(), rp.getCount(), true);
						success = 1;
					}
				}
			}
		}

		if(success == 0)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_MANUFACTURING_FAILURE).addItemName(recipe.getRecipeId()));
		}
		activeChar.sendPacket(new RecipeItemMakeInfo(activeChar, recipe, success));
	}
}