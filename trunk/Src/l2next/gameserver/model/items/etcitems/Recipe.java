package l2next.gameserver.model.items.etcitems;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ALF
 * @date 08.11.2012
 */
public class Recipe
{
	private final int id;
	private final int recipeId;
	private final int mpConsume;
	private final int craftLevel;
	private final boolean isCommon;
	private final int successRate;

	private Set<RecipeIngredient> ingredients;
	private Set<RecipeProduction> productions;

	public Recipe(int id, int recipeId, int mpConsume, int craftLevel, boolean isCommon, int successRate)
	{
		this.id = id;
		this.recipeId = recipeId;
		this.mpConsume = mpConsume;
		this.craftLevel = craftLevel;
		this.isCommon = isCommon;
		this.successRate = successRate;

		//ingredients = new HashSet<>();
		//productions = new HashSet<>();
		ingredients = new HashSet<RecipeIngredient>();
		productions = new HashSet<RecipeProduction>();
	}

	public int getId()
	{
		return id;
	}

	public int getRecipeId()
	{
		return recipeId;
	}

	public int getMpConsume()
	{
		return mpConsume;
	}

	public int getCraftLevel()
	{
		return craftLevel;
	}

	public boolean isCommon()
	{
		return isCommon;
	}

	public int getSuccessRate()
	{
		return successRate;
	}

	public void addIngradient(RecipeIngredient ri)
	{
		ingredients.add(ri);
	}

	public Set<RecipeIngredient> getIngredients()
	{
		return ingredients;
	}

	public void addProduction(RecipeProduction ri)
	{
		productions.add(ri);
	}

	public Set<RecipeProduction> getProductions()
	{
		return productions;
	}

}
