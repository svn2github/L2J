package npc.model;

import l2next.commons.util.Rnd;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.templates.npc.NpcTemplate;

public class BergamoChestInstance extends NpcInstance
{
	/**
	 * @Author Awakeninger
	 */
	 
	public static enum BergamoState
	{
		Start, 
		Open;
	}
	
	private void setState(BergamoState state)
	{
		_state = state;
	}
	
	private BergamoState _state;
	private static final long serialVersionUID = 2446684686394123656L;
	private static final int[] dropped = new int[]{
		6569,
		6570,
		6571,
		6572,
		6573,
		6574,
		6575,
		6576,
		6577,
		6578,
		19447,
		19448,
		9546,
		9547,
		9548,
		9549,
		9550,
		9551,
		9552,
		9553,
		9554,
		9555,
		9556,
		9557
	};
	private static NpcInstance npc;

	public BergamoChestInstance(int objectId, NpcTemplate template)
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
		
		setState(BergamoState.Start);
		if(command.equalsIgnoreCase("start") && _state == BergamoState.Start)
		{
			setState(BergamoState.Open);
			this.doDie(null);
			dropItem(player, dropped[Rnd.get(dropped.length)], 1);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}

	@Override
	public void showChatWindow(Player player, int val, Object... replace)
	{
		NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
		if(_state == BergamoState.Open)
		{
			htmlMessage.setFile("openBergamoChest.htm");
		}
		else
		{
			htmlMessage.setFile("default/" + getNpcId() + ".htm");
		}
		player.sendPacket(htmlMessage);
	}

}