package l2next.gameserver.dao;

import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.data.xml.holder.RecipeHolder;
import l2next.gameserver.database.DatabaseFactory;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.etcitems.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Дмитрий
 * @date 07.11.12  14:05
 */
public class CharacterRecipebookDAO
{
	private static final Logger log = LoggerFactory.getLogger(CharacterPostFriendDAO.class);
	private static final String SELECT_QUERY = "SELECT id FROM character_recipebook WHERE char_id=?";
	private static final String REPLACE_QUERY = "REPLACE INTO character_recipebook (char_id, id) VALUES(?,?)";
	private static final String DELETE_QUERY = "DELETE FROM `character_recipebook` WHERE `char_id`=? AND `id`=? LIMIT 1";
	private static CharacterRecipebookDAO ourInstance = new CharacterRecipebookDAO();

	private CharacterRecipebookDAO()
	{
	}

	public static CharacterRecipebookDAO getInstance()
	{
		return ourInstance;
	}

	public void restoreRecipeBook(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setInt(1, player.getObjectId());
			rset = statement.executeQuery();

			while(rset.next())
			{
				int id = rset.getInt("id");
				Recipe recipe = RecipeHolder.getInstance().getRecipeById(id);
				if(recipe.isCommon())
				{
					player.addCommonRecipe(recipe);
				}
				else
				{
					player.addDwarfRecipe(recipe);
				}
			}
		}
		catch(Exception e)
		{
			log.warn("count not recipe skills:" + e);
			log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public void registerRecipe(int playerObjId, int recipeId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(REPLACE_QUERY);
			statement.setInt(1, playerObjId);
			statement.setInt(2, recipeId);
			statement.executeUpdate();
		}
		catch(Exception e)
		{
			log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void unregisterRecipe(int playerObjId, int recipeId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_QUERY);
			statement.setInt(1, playerObjId);
			statement.setInt(2, recipeId);
			statement.executeUpdate();
		}
		catch(Exception e)
		{
			log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
