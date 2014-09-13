package npc.model;

import l2next.gameserver.instancemanager.HellboundManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.skills.AbnormalEffect;
import l2next.gameserver.templates.npc.NpcTemplate;

import java.util.StringTokenizer;

/**
 * Данный инстанс используется в городе-инстансе на Hellbound
 *
 * @author SYS
 */
public final class NativePrisonerInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 4136148865807978368L;

	public NativePrisonerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	protected void onSpawn()
	{
		startAbnormalEffect(AbnormalEffect.HOLD_2);
		super.onSpawn();
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this) || isBusy())
		{
			return;
		}

		StringTokenizer st = new StringTokenizer(command);
		if(st.nextToken().equals("rescue"))
		{
			stopAbnormalEffect(AbnormalEffect.HOLD_2);
			Functions.npcSay(this, "Thank you for saving me! Guards are coming, run!");
			HellboundManager.addConfidence(15);
			deleteMe();
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}