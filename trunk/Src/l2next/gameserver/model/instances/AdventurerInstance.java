package l2next.gameserver.model.instances;

import l2next.commons.util.Rnd;
import l2next.gameserver.instancemanager.RaidBossSpawnManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Spawner;
import l2next.gameserver.network.serverpackets.ExShowQuestInfo;
import l2next.gameserver.network.serverpackets.RadarControl;
import l2next.gameserver.network.serverpackets.components.CustomMessage;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.ItemFunctions;
import l2next.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventurerInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 6281802299335487371L;
	private static final Logger _log = LoggerFactory.getLogger(AdventurerInstance.class);
	private boolean _validateItem = true;

	public AdventurerInstance(int objectId, NpcTemplate template)
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

		if(command.startsWith("npcfind_byid"))
		{
			try
			{
				int bossId = Integer.parseInt(command.substring(12).trim());
				switch(RaidBossSpawnManager.getInstance().getRaidBossStatusId(bossId))
				{
					case ALIVE:
					case DEAD:
						Spawner spawn = RaidBossSpawnManager.getInstance().getSpawnTable().get(bossId);

						Location loc = spawn.getCurrentSpawnRange().getRandomLoc(spawn.getReflection().getGeoIndex());

						// Убираем и ставим флажок на карте и стрелку на компасе
						player.sendPacket(new RadarControl(2, 2, loc), new RadarControl(0, 1, loc));
						break;
					case UNDEFINED:
						player.sendMessage(new CustomMessage("l2next.gameserver.model.instances.L2AdventurerInstance.BossNotInGame", player).addNumber(bossId));
						break;
				}
			}
			catch(NumberFormatException e)
			{
				_log.warn("AdventurerInstance: Invalid Bypass to Server command parameter.");
			}
		}
		else if(command.startsWith("raidInfo"))
		{
			int bossLevel = Integer.parseInt(command.substring(9).trim());

			String filename = "adventurer_guildsman/raid_info/info.htm";
			if(bossLevel != 0)
			{
				filename = "adventurer_guildsman/raid_info/level" + bossLevel + ".htm";
			}

			showChatWindow(player, filename);
		}
		else if(command.startsWith("tipInfo"))
		{
			showChatWindow(player, "adventurer_guildsman/tip/" + Rnd.get(0, 27) + "-tipinfo.htm");
		}
		else if(command.equalsIgnoreCase("questlist"))
		{
			player.sendPacket(ExShowQuestInfo.STATIC);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		if(getNpcId() == 33463 && _validateItem)
		{
			ItemFunctions.addItem(player, 32241, 1, true);
			_validateItem = false;
			player.sendPacket(SystemMsg.A_MARK_OF_ADVENTURER_IS_ACQUIRED_THIS_ITEM_CAN_BE_RE_ACQUIRED_EVERYDAY);
		}
		else
		{
			player.sendPacket(SystemMsg.A_MARK_OF_ADVENTURER_IS_ACQUIRED_THIS_ITEM_CAN_BE_RE_ACQUIRED_EVERYDAY);
		}
		super.showChatWindow(player, val, arg);
	}

	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String pom;
		if(val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}

		return "adventurer_guildsman/" + pom + ".htm";
	}
}