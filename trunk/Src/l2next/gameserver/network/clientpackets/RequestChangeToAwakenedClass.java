package l2next.gameserver.network.clientpackets;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.Config;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.instancemanager.AwakingManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.ProfessionRewards;
import l2next.gameserver.network.serverpackets.ExShowUsmVideo;

/**
 * @Author Awakeninger
 */
public class RequestChangeToAwakenedClass extends L2GameClientPacket
{
	private static final int SCROLL_OF_AFTERLIFE = 17600;
	private int change;
	private int MY_CLASS_ID;
	private int MY_OLD_CLASS_ID;

	@Override
	protected void readImpl() throws Exception
	{
		this.change = readD();
	}

	@Override
	protected void runImpl() throws Exception
	{
		if(!Config.AWAKING_FREE)
		{
			final Player player = getClient().getActiveChar();
			if(player == null)
			{
				return;
			}
			if(change != 1)
			{
				return;
			}
			AwakingManager.getInstance().SetAwakingId(player);
			if(player.getVar("AwakenedID") != null)
			{
				MY_CLASS_ID = player.getVarInt("AwakenedID");
				ProfessionRewards.checkedAndGiveChest(player, MY_CLASS_ID);
			}
			
			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl() throws Exception
				{
					player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
				}
			}, 15000);
		}
		else
		{
			Player player = getClient().getActiveChar();

			if(player == null)
			{
				return;
			}

			if(player.getLevel() < 85)
			{
				return;
			}

			if(player.getClassId().level() < 3)
			{
				return;
			}

			if(player.isAwaking())
			{
				return;
			}

			AwakingManager.getInstance().SetAwakingId(player);
		}
	}
}
