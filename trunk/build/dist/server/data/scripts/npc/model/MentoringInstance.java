package npc.model;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.ItemFunctions;

/**
 * @author KilRoy
 */
public class MentoringInstance extends NpcInstance
{
	private static final long serialVersionUID = 907679679965868534L;

	private int MENTEE_CERTIFICATE = 33800;
	private int DIPLOMA = 33805;

	public MentoringInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}
		if(command.startsWith("exchangediploma"))
		{
			if(player.isAwaking())
			{
				if(player.getLevel() > 85)
				{
					showChatWindow(player, "default/" + getNpcId() + "-diploma.htm");
				}
				else
				{
					showChatWindow(player, "default/" + getNpcId() + "-no-diploma.htm");
				}
			}
			else
			{
				showChatWindow(player, "default/" + getNpcId() + "-no-diploma.htm");
			}
		}
		else if(command.startsWith("exchangeverefi"))
		{
			if(ItemFunctions.getItemCount(player, MENTEE_CERTIFICATE) == 1)
			{
				ItemFunctions.removeItem(player, MENTEE_CERTIFICATE, 1, true);
				ItemFunctions.addItem(player, DIPLOMA, 40, true);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}