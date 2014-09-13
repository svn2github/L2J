package events;

import l2next.gameserver.listener.actor.OnDeathListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.actor.listener.CharListenerList;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.scripts.ScriptFile;

/**
 *
 * @author Awakeninger
 */
public class SignDrop extends Functions implements ScriptFile, OnDeathListener
{

	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
	}

	@Override
	public void onReload()
	{
		//dummy
	}

	@Override
	public void onShutdown()
	{
		//dummy
	}

	@Override
	public void onDeath(Creature actor, Creature killer)
	{
		int ItemID = 0;
		if(SimpleCheckDropWithMonster(actor, killer))
		{
			final int actorLvl = actor.getLevel();
			if(actorLvl >= 85 && actorLvl <= 89)
			{
				ItemID = 17739;
			}
			else if(actorLvl >= 90 && actorLvl <= 94)
			{
				ItemID = 17740;
			}
			else if(actorLvl >= 95 && actorLvl <= 98)
			{
				ItemID = 17741;
			}
			else if(actorLvl == 99)
			{
				ItemID = 17742;
			}

			if(killer.getPlayer().getVar("HaveSign") == null)
			{
				addItem(killer.getPlayer(), ItemID, 1);
				killer.getPlayer().setVar("HaveSign", "@give", 24 * 60 * 60 * 1000L);
			}
		}
	}
}
