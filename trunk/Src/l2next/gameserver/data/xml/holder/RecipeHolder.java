package l2next.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2next.commons.data.xml.AbstractHolder;
import l2next.gameserver.model.items.etcitems.Recipe;

import java.util.Collection;

public final class RecipeHolder extends AbstractHolder
{
	private static final RecipeHolder _instance = new RecipeHolder();

	//private final TIntObjectHashMap<Recipe> _listByRecipeId = new TIntObjectHashMap<>();
	private final TIntObjectHashMap<Recipe> _listByRecipeId = new TIntObjectHashMap<Recipe>();
	//private final TIntObjectHashMap<Recipe> _listByRecipeItem = new TIntObjectHashMap<>();
	private final TIntObjectHashMap<Recipe> _listByRecipeItem = new TIntObjectHashMap<Recipe>();

	public static RecipeHolder getInstance()
	{
		return _instance;
	}

	public void addRecipe(Recipe recipe)
	{
		_listByRecipeId.put(recipe.getId(), recipe);
		_listByRecipeItem.put(recipe.getRecipeId(), recipe);
	}

	public Recipe getRecipeById(int id)
	{
		return _listByRecipeId.get(id);
	}

	public Recipe getRecipeByRecipeItem(int id)
	{
		return _listByRecipeItem.get(id);
	}

	public Collection<Recipe> getRecipes()
	{
		return _listByRecipeId.valueCollection();
	}

	@Override
	public int size()
	{
		return _listByRecipeId.size();
	}

	@Override
	public void clear()
	{
		_listByRecipeId.clear();
		_listByRecipeItem.clear();
	}
}
