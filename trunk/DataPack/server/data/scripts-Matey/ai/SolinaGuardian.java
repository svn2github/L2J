package ai;

import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.tables.SkillTable;

/**
 * @author pchayka
 */
public class SolinaGuardian extends Fighter
{

	public SolinaGuardian(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		getActor().altOnMagicUseTimer(getActor(), SkillTable.getInstance().getInfo(6371, 1));
	}
}