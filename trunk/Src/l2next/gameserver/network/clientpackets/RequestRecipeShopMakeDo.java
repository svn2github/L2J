package l2next.gameserver.network.clientpackets;

import l2next.commons.util.Rnd;
import l2next.gameserver.Config;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.data.xml.holder.RecipeHolder;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.ManufactureItem;
import l2next.gameserver.model.items.etcitems.Recipe;
import l2next.gameserver.model.items.etcitems.RecipeIngredient;
import l2next.gameserver.model.items.etcitems.RecipeProduction;
import l2next.gameserver.network.serverpackets.RecipeShopItemInfo;
import l2next.gameserver.network.serverpackets.StatusUpdate;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.CustomMessage;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.utils.ItemFunctions;
import l2next.gameserver.utils.TradeHelper;

public class RequestRecipeShopMakeDo extends L2GameClientPacket
{
	private int _manufacturerId;
	private int _recipeId;
	private long _price;

	@Override
	protected void readImpl()
	{
		_manufacturerId = readD();
		_recipeId = readD();
		_price = readQ();
	}

	@Override
	protected void runImpl()
	{
		Player buyer = getClient().getActiveChar();
		if(buyer == null)
		{
			return;
		}

		if(buyer.isActionsDisabled())
		{
			buyer.sendActionFailed();
			return;
		}

		if(buyer.isInStoreMode())
		{
			buyer.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}

		if(buyer.isInTrade())
		{
			buyer.sendActionFailed();
			return;
		}

		if(buyer.isFishing())
		{
			buyer.sendPacket(Msg.YOU_CANNOT_DO_ANYTHING_ELSE_WHILE_FISHING);
			return;
		}

		if(!buyer.getPlayerAccess().UseTrade)
		{
			buyer.sendPacket(Msg.THIS_ACCOUNT_CANOT_USE_PRIVATE_STORES);
			return;
		}

		Player manufacturer = (Player) buyer.getVisibleObject(_manufacturerId);
		if(manufacturer == null || manufacturer.getPrivateStoreType() != Player.STORE_PRIVATE_MANUFACTURE || !manufacturer.isInRangeZ(buyer, Creature.INTERACTION_DISTANCE))
		{
			buyer.sendActionFailed();
			return;
		}

		Recipe recipeList = null;
		for(ManufactureItem mi : manufacturer.getCreateList())
		{
			if(mi.getRecipeId() == _recipeId)
			{
				if(_price == mi.getCost())
				{
					recipeList = RecipeHolder.getInstance().getRecipeById(_recipeId);
					break;
				}
			}
		}

		if(recipeList == null)
		{
			buyer.sendActionFailed();
			return;
		}

		int success = 0;

		if(recipeList.getIngredients().isEmpty() || recipeList.getProductions().isEmpty())
		{
			manufacturer.sendPacket(SystemMsg.THE_RECIPE_IS_INCORRECT);
			buyer.sendPacket(SystemMsg.THE_RECIPE_IS_INCORRECT);
			return;
		}

		if(!manufacturer.findRecipe(_recipeId))
		{
			buyer.sendActionFailed();
			return;
		}

		if(manufacturer.getCurrentMp() < recipeList.getMpConsume())
		{
			manufacturer.sendPacket(Msg.NOT_ENOUGH_MP);
			buyer.sendPacket(Msg.NOT_ENOUGH_MP, new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
			return;
		}

		buyer.getInventory().writeLock();
		try
		{
			if(buyer.getAdena() < _price)
			{
				buyer.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA, new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
				return;
			}

			for(RecipeIngredient recipe : recipeList.getIngredients())
			{
				if(recipe.getCount() == 0)
				{
					continue;
				}

				ItemInstance item = buyer.getInventory().getItemByItemId(recipe.getItemId());

				if(item == null || recipe.getCount() > item.getCount())
				{
					buyer.sendPacket(Msg.NOT_ENOUGH_MATERIALS, new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
					return;
				}
			}

			if(!buyer.reduceAdena(_price, false))
			{
				buyer.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA, new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
				return;
			}

			for(RecipeIngredient recipe : recipeList.getIngredients())
			{
				if(recipe.getCount() != 0)
				{
					buyer.getInventory().destroyItemByItemId(recipe.getItemId(), recipe.getCount());
					//TODO audit
					buyer.sendPacket(SystemMessage2.removeItems(recipe.getItemId(), recipe.getCount()));
				}
			}

			long tax = TradeHelper.getTax(manufacturer, _price);
			if(tax > 0)
			{
				_price -= tax;
				manufacturer.sendMessage(new CustomMessage("trade.HavePaidTax", manufacturer).addNumber(tax));
			}

			manufacturer.addAdena(_price);
		}
		finally
		{
			buyer.getInventory().writeUnlock();
		}

		//manufacturer.sendMessage(new CustomMessage("l2p.gameserver.RecipeController.GotOrder", manufacturer).addString(recipeList.getRecipeName()));

		manufacturer.reduceCurrentMp(recipeList.getMpConsume(), null);
		manufacturer.sendStatusUpdate(false, false, StatusUpdate.CUR_MP);

		int tryCount = 1, successCount = 0;
		if(Rnd.chance(Config.CRAFT_DOUBLECRAFT_CHANCE))
		{
			tryCount++;
		}

		for(int i = 0; i < tryCount; i++)
		{
			if(Rnd.chance(recipeList.getSuccessRate()))
			{
				for(RecipeProduction rp : recipeList.getProductions())
				{
					// TODO [G1ta0] добавить проверку на перевес
					if(Rnd.chance(rp.getChance()))
					{
						ItemFunctions.addItem(buyer, rp.getItemId(), rp.getCount(), true);
						success = 1;
						successCount++;
					}
				}
			}
		}

		SystemMessage sm;
		if(successCount == 0)
		{
			sm = new SystemMessage(SystemMessage.S1_HAS_FAILED_TO_CREATE_S2_AT_THE_PRICE_OF_S3_ADENA);
			sm.addString(manufacturer.getName());
			sm.addItemName(recipeList.getRecipeId());
			sm.addNumber(_price);
			buyer.sendPacket(sm);

			sm = new SystemMessage(SystemMessage.THE_ATTEMPT_TO_CREATE_S2_FOR_S1_AT_THE_PRICE_OF_S3_ADENA_HAS_FAILED);
			sm.addString(buyer.getName());
			sm.addItemName(recipeList.getRecipeId());
			sm.addNumber(_price);
			manufacturer.sendPacket(sm);

		}
		else if(successCount > 1)
		{
			sm = new SystemMessage(SystemMessage.S1_CREATED_S2_S3_AT_THE_PRICE_OF_S4_ADENA);
			sm.addString(manufacturer.getName());
			sm.addItemName(recipeList.getRecipeId());
			sm.addNumber(successCount);
			sm.addNumber(_price);
			buyer.sendPacket(sm);

			sm = new SystemMessage(SystemMessage.S2_S3_HAVE_BEEN_SOLD_TO_S1_FOR_S4_ADENA);
			sm.addString(buyer.getName());
			sm.addItemName(recipeList.getRecipeId());
			sm.addNumber(successCount);
			sm.addNumber(_price);
			manufacturer.sendPacket(sm);

		}
		else
		{
			sm = new SystemMessage(SystemMessage.S1_CREATED_S2_AFTER_RECEIVING_S3_ADENA);
			sm.addString(manufacturer.getName());
			sm.addItemName(recipeList.getRecipeId());
			sm.addNumber(_price);
			buyer.sendPacket(sm);

			sm = new SystemMessage(SystemMessage.S2_IS_SOLD_TO_S1_AT_THE_PRICE_OF_S3_ADENA);
			sm.addString(buyer.getName());
			sm.addItemName(recipeList.getRecipeId());
			sm.addNumber(_price);
			manufacturer.sendPacket(sm);
		}

		buyer.sendChanges();
		buyer.sendPacket(new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
	}
}