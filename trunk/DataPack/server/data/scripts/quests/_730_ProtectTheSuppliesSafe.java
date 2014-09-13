package quests;

import l2next.gameserver.data.xml.holder.EventHolder;
import l2next.gameserver.model.entity.events.EventType;
import l2next.gameserver.model.entity.events.impl.DominionSiegeRunnerEvent;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.scripts.ScriptFile;

/**
 * @author VISTALL
 * @date 8:05/10.06.2011
 */
public class _730_ProtectTheSuppliesSafe extends Quest implements ScriptFile
{
	public _730_ProtectTheSuppliesSafe()
	{
		super(PARTY_NONE);
		DominionSiegeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
		runnerEvent.addBreakQuest(this);
	}

	@Override
	public void onLoad()
	{

	}

	@Override
	public void onReload()
	{

	}

	@Override
	public void onShutdown()
	{

	}
}
