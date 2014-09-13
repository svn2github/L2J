package l2next.gameserver.idfactory;

import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.database.DatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ALF
 */
public class Tasks
{
	private static final Logger _log = LoggerFactory.getLogger(Tasks.class);

	public static enum ClearQuery implements Runnable
	{
		/**
		 * Чистка таблиц, связаных с игроками, которые были удалены. Или же с объктами, пренадлежащимы несуществующим игрокам
		 */
		character_blocklist("DELETE FROM character_blocklist WHERE character_blocklist.obj_Id NOT IN (SELECT obj_Id FROM characters);"), character_bookmarks("DELETE FROM character_bookmarks WHERE character_bookmarks.char_Id NOT IN (SELECT obj_Id FROM characters);"), character_effects_save("DELETE FROM character_effects_save WHERE character_effects_save.object_id NOT IN (SELECT obj_Id FROM characters);"), character_friends("DELETE FROM character_friends WHERE character_friends.char_id NOT IN (SELECT obj_Id FROM characters);"), character_group_reuse("DELETE FROM character_group_reuse WHERE character_group_reuse.object_id NOT IN (SELECT obj_Id FROM characters);"), character_hennas("DELETE FROM character_hennas WHERE character_hennas.char_obj_id NOT IN (SELECT obj_Id FROM characters);"), character_instances("DELETE FROM character_instances WHERE character_instances.obj_id NOT IN (SELECT obj_Id FROM characters);"), character_macroses("DELETE FROM character_macroses WHERE character_macroses.char_obj_id NOT IN (SELECT obj_Id FROM characters);"), character_minigame_score("DELETE FROM character_minigame_score WHERE character_minigame_score.object_id NOT IN (SELECT obj_Id FROM characters);"), character_post_friends("DELETE FROM character_post_friends WHERE character_post_friends.object_id NOT IN (SELECT obj_Id FROM characters);"), character_premium_items("DELETE FROM character_premium_items WHERE character_premium_items.charId NOT IN (SELECT obj_Id FROM characters);"), character_quests("DELETE FROM character_quests WHERE character_quests.char_id NOT IN (SELECT obj_Id FROM characters);"), character_recipebook("DELETE FROM character_recipebook WHERE character_recipebook.char_id NOT IN (SELECT obj_Id FROM characters);"), character_shortcuts("DELETE FROM character_shortcuts WHERE character_shortcuts.object_id NOT IN (SELECT obj_Id FROM characters);"), character_skills("DELETE FROM character_skills WHERE character_skills.char_obj_id NOT IN (SELECT obj_Id FROM characters);"), character_skills_save("DELETE FROM character_skills_save WHERE character_skills_save.char_obj_id NOT IN (SELECT obj_Id FROM characters);"), character_subclasses("DELETE FROM character_subclasses WHERE character_subclasses.char_obj_id NOT IN (SELECT obj_Id FROM characters);"), character_variables("DELETE FROM character_variables WHERE character_variables.obj_id NOT IN (SELECT obj_Id FROM characters);"),

		//		items("DELETE FROM items WHERE items.owner_id NOT IN (SELECT obj_Id FROM characters);"),

		heroes("DELETE FROM heroes WHERE heroes.char_id NOT IN (SELECT obj_Id FROM characters);"), olympiad_nobles("DELETE FROM olympiad_nobles WHERE olympiad_nobles.char_id NOT IN (SELECT obj_Id FROM characters);");

		public static int totalDeleted = 0, totalUpdated = 0;
		private static ReentrantLock totalLock = new ReentrantLock();
		private final ClearQuery _parent;
		public final String _query, _table;
		public final boolean _update;
		public boolean compleated;

		private ClearQuery(String query, ClearQuery parent)
		{
			compleated = false;
			_query = query;
			_parent = parent;
			_update = name().startsWith("update_");
			_table = _update ? name().replaceFirst("update_", "") : name();
		}

		private ClearQuery(String query)
		{
			this(query, null);
		}

		@Override
		public void run()
		{
			Connection con = null;
			Statement s = null;
			ResultSet rs = null;

			try
			{
				if(_parent != null)
				{
					while(!_parent.compleated)
					{
						synchronized(_parent)
						{
							_parent.wait();
						}
					}
				}
				con = DatabaseFactory.getInstance().getConnection();
				s = con.createStatement();
				int currCount = s.executeUpdate(_query);
				if(currCount > 0)
				{
					totalLock.lock();
					if(_update)
					{
						totalUpdated += currCount;
					}
					else
					{
						totalDeleted += currCount;
					}
					totalLock.unlock();
					if(_update)
					{
						_log.info("Updated " + currCount + " elements in table " + _table + ".");
					}
					else
					{
						_log.info("Cleaned " + currCount + " elements from table " + _table + ".");
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				DbUtils.closeQuietly(con, s, rs);
				compleated = true;
				synchronized(this)
				{
					notifyAll();
				}
			}
		}
	}
}