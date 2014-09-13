package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.etcitems.Recipe;

import java.util.Collection;

public class RecipeBookItemList extends L2GameServerPacket
{
	private Collection<Recipe> _recipes;
	private final boolean _isDwarvenCraft;
	private final int _currentMp;

	public RecipeBookItemList(Player player, boolean isDwarvenCraft)
	{
		_isDwarvenCraft = isDwarvenCraft;
		_currentMp = (int) player.getCurrentMp();
		if(isDwarvenCraft)
		{
			_recipes = player.getDwarvenRecipeBook();
		}
		else
		{
			_recipes = player.getCommonRecipeBook();
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_isDwarvenCraft ? 0x00 : 0x01);
		writeD(_currentMp);

		writeD(_recipes.size());

		for(Recipe recipe : _recipes)
		{
			writeD(recipe.getId());
			writeD(1); // ??
		}
	}
}