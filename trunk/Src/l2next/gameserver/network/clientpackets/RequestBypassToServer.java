package l2next.gameserver.network.clientpackets;

import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.MultiSellHolder;
import l2next.gameserver.handler.admincommands.AdminCommandHandler;
import l2next.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2next.gameserver.handler.voicecommands.VoicedCommandHandler;
import l2next.gameserver.instancemanager.BypassManager.DecodedBypass;
import l2next.gameserver.instancemanager.OlympiadHistoryManager;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.GameObject;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Hero;
import l2next.gameserver.model.entity.olympiad.Olympiad;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.instances.OlympiadManagerInstance;
import l2next.gameserver.model.quest.dynamic.DynamicQuestController;
import l2next.gameserver.network.GameClient;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.scripts.Scripts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RequestBypassToServer extends L2GameClientPacket
{
	// Format: cS
	private static final Logger _log = LoggerFactory.getLogger(RequestBypassToServer.class);
	private DecodedBypass bp = null;

	private static void comeHere(GameClient client)
	{
		GameObject obj = client.getActiveChar().getTarget();
		if(obj != null && obj.isNpc())
		{
			NpcInstance temp = (NpcInstance) obj;
			Player activeChar = client.getActiveChar();
			temp.setTarget(activeChar);
			temp.moveToLocation(activeChar.getLoc(), 0, true);
		}
	}

	private static void playerHelp(Player activeChar, String path)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(5);
		html.setFile(path);
		activeChar.sendPacket(html);
	}

	@Override
	protected void readImpl()
	{
		String bypass = readS();
		if(!bypass.isEmpty())
		{
			bp = getClient().getActiveChar().decodeBypass(bypass);
		}
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null || bp == null)
		{
			return;
		}
		try
		{
			NpcInstance npc = activeChar.getLastNpc();
			GameObject target = activeChar.getTarget();
			if(npc == null && target != null && target.isNpc())
			{
				npc = (NpcInstance) target;
			}

			if(bp.bypass.startsWith("admin_"))
			{
				AdminCommandHandler.getInstance().useAdminCommandHandler(activeChar, bp.bypass);
			}
			else if(bp.bypass.equals("come_here") && activeChar.isGM())
			{
				comeHere(getClient());
			}
			else if(bp.bypass.startsWith("player_help "))
			{
				playerHelp(activeChar, bp.bypass.substring(12));
			}
			else if(bp.bypass.startsWith("scripts_"))
			{
				String command = bp.bypass.substring(8).trim();
				String[] word = command.split("\\s+");
				String[] args = command.substring(word[0].length()).trim().split("\\s+");
				String[] path = word[0].split(":");
				if(path.length != 2)
				{
					_log.warn("Bad Script bypass!");
					return;
				}

				Map<String, Object> variables = null;
				if(npc != null)
				{
					//variables = new HashMap<>(1);
					variables = new HashMap<String, Object>(1);
					variables.put("npc", npc.getRef());
				}

				if(word.length == 1)
				{
					Scripts.getInstance().callScripts(activeChar, path[0], path[1], variables);
				}
				else
				{
					Scripts.getInstance().callScripts(activeChar, path[0], path[1], new Object[]{args}, variables);
				}
			}
			else if(bp.bypass.startsWith("user_"))
			{
				String command = bp.bypass.substring(5).trim();
				String word = command.split("\\s+")[0];
				String args = command.substring(word.length()).trim();
				IVoicedCommandHandler vch = VoicedCommandHandler.getInstance().getVoicedCommandHandler(word);

				if(vch != null)
				{
					vch.useVoicedCommand(word, activeChar, args);
				}
				else
				{
					_log.warn("Unknow voiced command '" + word + "'");
				}
			}
			else if(bp.bypass.startsWith("npc_"))
			{
				int endOfId = bp.bypass.indexOf('_', 5);
				String id;
				if(endOfId > 0)
				{
					id = bp.bypass.substring(4, endOfId);
				}
				else
				{
					id = bp.bypass.substring(4);
				}
				GameObject object = activeChar.getVisibleObject(Integer.parseInt(id));
				if(object != null && object.isNpc() && endOfId > 0 && activeChar.isInRange(object.getLoc(), Creature.INTERACTION_DISTANCE))
				{
					activeChar.setLastNpc((NpcInstance) object);
					((NpcInstance) object).onBypassFeedback(activeChar, bp.bypass.substring(endOfId + 1));
				}
			}
			else if(bp.bypass.startsWith("_olympiad?"))
			{
				String[] ar = bp.bypass.replace("_olympiad?", "").split("&");
				String firstVal = ar[0].split("=")[1];
				String secondVal = ar[1].split("=")[1];

				if(firstVal.equalsIgnoreCase("move_op_field"))
				{
					if(!Config.ENABLE_OLYMPIAD_SPECTATING)
					{
						return;
					}

					// Переход в просмотр олимпа разрешен только от менеджера
					// или с арены.
					if(activeChar.getLastNpc() instanceof OlympiadManagerInstance && activeChar.getLastNpc().isInRange(activeChar, Creature.INTERACTION_DISTANCE) || activeChar.getOlympiadObserveGame() != null)
					{
						Olympiad.addSpectator(Integer.parseInt(secondVal) - 1, activeChar);
					}
				}
			}
			else if(bp.bypass.startsWith("_diary"))
			{
				String params = bp.bypass.substring(bp.bypass.indexOf("?") + 1);
				StringTokenizer st = new StringTokenizer(params, "&");
				int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
				int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
				int heroid = Hero.getInstance().getHeroByClass(heroclass);
				if(heroid > 0)
				{
					Hero.getInstance().showHeroDiary(activeChar, heroclass, heroid, heropage);
				}
			}
			else if(bp.bypass.startsWith("_match"))
			{
				String params = bp.bypass.substring(bp.bypass.indexOf("?") + 1);
				StringTokenizer st = new StringTokenizer(params, "&");
				int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
				int heropage = Integer.parseInt(st.nextToken().split("=")[1]);

				OlympiadHistoryManager.getInstance().showHistory(activeChar, heroclass, heropage);
			}
			else if(bp.bypass.startsWith("manor_menu_select?")) // Navigate
			// throught
			// Manor
			// windows
			{
				GameObject object = activeChar.getTarget();
				if(object != null && object.isNpc())
				{
					((NpcInstance) object).onBypassFeedback(activeChar, bp.bypass);
				}
			}
			else if(bp.bypass.startsWith("multisell "))
			{
				MultiSellHolder.getInstance().SeparateAndSend(Integer.parseInt(bp.bypass.substring(10)), activeChar, 0);
			}
			else if(bp.bypass.startsWith("menu_select?"))
			{
				String params = bp.bypass.substring(bp.bypass.indexOf("?") + 1);
				StringTokenizer st = new StringTokenizer(params, "&");
				int ask = Integer.parseInt(st.nextToken().split("=")[1]);
				int reply = Integer.parseInt(st.nextToken().split("=")[1]);
				if(npc != null)
				{
					npc.onMenuSelect(activeChar, ask, reply);
				}
			}
			else if(bp.bypass.startsWith("Quest "))
			{
				String p = bp.bypass.substring(6).trim();
				int idx = p.indexOf(' ');
				if(idx < 0)
				{
					activeChar.processQuestEvent(p, "", npc);
				}
				else
				{
					activeChar.processQuestEvent(p.substring(0, idx), p.substring(idx).trim(), npc);
				}
			}
			else if(bp.bypass.startsWith("Campaign "))
			{
				String p = bp.bypass.substring(9).trim();

				int idx = p.indexOf(' ');
				if(idx > 0)
				{
					String campaignName = p.substring(0, idx);
					DynamicQuestController.getInstance().processDialogEvent(campaignName, p.substring(idx).trim(), activeChar);
				}
			}
			else if(bp.handler != null)
			{
				if(!Config.COMMUNITYBOARD_ENABLED)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE));
				}
				else
				{
					bp.handler.onBypassCommand(activeChar, bp.bypass);
				}
			}
		}
		catch(Exception e)
		{
			// _log.error("", e);
			String st = "Bad RequestBypassToServer: " + bp.bypass;
			GameObject target = activeChar.getTarget();
			if(target != null && target.isNpc())
			{
				st = st + " via NPC #" + ((NpcInstance) target).getNpcId();
			}
			_log.error(st, e);
		}
	}
}