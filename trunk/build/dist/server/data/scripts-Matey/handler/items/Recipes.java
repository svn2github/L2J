package handler.items;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.dao.CharacterRecipebookDAO;
import l2next.gameserver.data.xml.holder.RecipeHolder;
import l2next.gameserver.model.Playable;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.etcitems.Recipe;
import l2next.gameserver.network.serverpackets.RecipeBookItemList;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.network.serverpackets.components.SystemMsg;

import java.util.Collection;

public class Recipes extends ScriptItemHandler
{
	private static int[] _itemIds = null;

	public Recipes()
	{
		Collection<Recipe> rc = RecipeHolder.getInstance().getRecipes();
		_itemIds = new int[rc.size()];
		int i = 0;
		for(Recipe r : rc)
		{
			_itemIds[i++] = r.getRecipeId();
		}
	}

	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
		{
			return false;
		}
		Player player = (Player) playable;

		Recipe rp = RecipeHolder.getInstance().getRecipeByRecipeItem(item.getItemId());
		if(rp.isCommon())
		{
			if(player.getCommonRecipeLimit() > 0)
			{
				if(player.getCommonRecipeBook().size() >= player.getCommonRecipeLimit())
				{
					player.sendPacket(Msg.NO_FURTHER_RECIPES_MAY_BE_REGISTERED);
					return false;
				}
				if(player.findRecipe(rp.getId()))
				{
					player.sendPacket(Msg.THAT_RECIPE_IS_ALREADY_REGISTERED);
					return false;
				}
				if(!player.getInventory().destroyItem(item, 1L))
				{
					player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
					return false;
				}
				CharacterRecipebookDAO.getInstance().registerRecipe(player.getObjectId(), rp.getId());
				player.addCommonRecipe(rp);
				player.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BEEN_ADDED).addItemName(item.getItemId()));
				player.sendPacket(new RecipeBookItemList(player, false));
				return true;
			}
			else
			{
				player.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_REGISTER_A_RECIPE);
			}
		}
		else if(player.getDwarvenRecipeLimit() > 0)
		{
			if(player.getDwarvenRecipeBook().size() >= player.getDwarvenRecipeLimit())
			{
				player.sendPacket(Msg.NO_FURTHER_RECIPES_MAY_BE_REGISTERED);
				return false;
			}

			if(rp.getCraftLevel() > player.getSkillLevel(Skill.SKILL_CRAFTING))
			{
				player.sendPacket(Msg.CREATE_ITEM_LEVEL_IS_TOO_LOW_TO_REGISTER_THIS_RECIPE);
				return false;
			}
			if(player.findRecipe(rp.getId()))
			{
				player.sendPacket(Msg.THAT_RECIPE_IS_ALREADY_REGISTERED);
				return false;
			}
			if(!player.getInventory().destroyItem(item, 1L))
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				return false;
			}
			// add recipe to recipebook
			player.addDwarfRecipe(rp);
			CharacterRecipebookDAO.getInstance().registerRecipe(player.getObjectId(), rp.getId());
			player.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BEEN_ADDED).addItemName(item.getItemId()));
			player.sendPacket(new RecipeBookItemList(player, true));
			return true;
		}
		else
		{
			player.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_REGISTER_A_RECIPE);
		}

		return false;
	}

	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}