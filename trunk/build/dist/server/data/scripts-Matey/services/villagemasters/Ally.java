package services.villagemasters;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.VillageMasterInstance;
import l2next.gameserver.scripts.Functions;

public class Ally extends Functions
{
	public void CheckCreateAlly()
	{
		if(getNpc() == null || getSelf() == null)
		{
			return;
		}
		Player pl = getSelf();
		String htmltext = "ally-01.htm";
		if(pl.isClanLeader())
		{
			htmltext = "ally-02.htm";
		}
		((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/ally/" + htmltext);
	}

	public void CheckDissolveAlly()
	{
		if(getNpc() == null || getSelf() == null)
		{
			return;
		}
		Player pl = getSelf();
		String htmltext = "ally-01.htm";
		if(pl.isAllyLeader())
		{
			htmltext = "ally-03.htm";
		}
		((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/ally/" + htmltext);
	}
}