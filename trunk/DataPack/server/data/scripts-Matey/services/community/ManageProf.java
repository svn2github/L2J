package services.community;

import l2next.gameserver.Config;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.data.xml.holder.ItemHolder;
import l2next.gameserver.handler.bbs.CommunityBoardHandler;
import l2next.gameserver.handler.bbs.ICommunityBoardHandler;
import l2next.gameserver.instancemanager.AwakingManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.SubClass;
import l2next.gameserver.model.base.ClassId;
import l2next.gameserver.model.base.ClassLevel;
import l2next.gameserver.model.base.SubClassType;
import l2next.gameserver.network.serverpackets.ExSubjobInfo;
import l2next.gameserver.network.serverpackets.ShowBoard;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.templates.item.ItemTemplate;
import l2next.gameserver.utils.BbsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageProf implements ScriptFile, ICommunityBoardHandler
{

	private static final String[] _commands = {
		"_bbscareer;",
		"_bbsclass_change",
		"_bbsclass_upgrade"
	};

	private static final Logger _log = LoggerFactory.getLogger(ManageProf.class);
	int jobLevel = 0;

	@Override
	public void onLoad()
	{
		if(Config.COMMUNITYBOARD_ENABLED/* && Config.BBS_PVP_CB_ENABLED*/)
		{
			_log.info("CommunityBoard: Manage Career service loaded.");
			CommunityBoardHandler.getInstance().registerHandler(this);
		}
	}

	@Override
	public void onReload()
	{
		if(Config.COMMUNITYBOARD_ENABLED/* && Config.BBS_PVP_CB_ENABLED*/)
		{
			CommunityBoardHandler.getInstance().removeHandler(this);
		}
	}

	@Override
	public void onShutdown()
	{
	}

	@Override
	public String[] getBypassCommands()
	{
		return _commands;
	}

	@Override
	public void onBypassCommand(Player player, String bypass)
	{
		if(!player.checkAllowAction())
		{
			return;
		}

		if(bypass.equals(_commands[0]))
		{
			String html = HtmCache.getInstance().getNotNull("pages/career.htm", player);
			html = html.replace("%career%", String.valueOf(makeMessage(player)));
			ShowBoard.separateAndSend(BbsUtil.htmlAll(html, player), player);
		}
		else if(bypass.startsWith(_commands[1]))
		{
			String[] args = bypass.split(" ", -1);

			int val = Integer.parseInt(args[1]);
			long price = Long.parseLong(args[2]);
			if(player.getInventory().destroyItemByItemId(Config.CLASS_MASTERS_PRICE_ITEM_LIST[jobLevel], price))
			{
				changeClass(player, val);
				onBypassCommand(player, "_bbscareer;");
			}

			else if(Config.CLASS_MASTERS_PRICE_ITEM_LIST[jobLevel] == 57)
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
		}
		else if(bypass.startsWith(_commands[2]))
		{
			if(player.getLevel() < 80)
			{
				player.sendMessage("Вы еще слишком слабы! Приходите как получите 80 уровень!");
				return;
			}

			if(player.getActiveSubClass().isBase())
			{
				player.sendMessage("Вы должны быть на саб-классе");
				return;
			}

			for(SubClass s : player.getSubClassList().values())
			{
				if(s.isDouble())
				{
					player.sendMessage("Вы уже имеете дуал-класс!");
					return;
				}
			}

			player.getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);

			AwakingManager.getInstance().onPlayerEnter(player);

			player.sendMessage("Поздравляем! Вы получили Дуал-Класс");
			player.sendPacket(new ExSubjobInfo(player, true));
		}

	}

	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{

	}

	private String makeMessage(Player player)
	{
		ClassId classId = player.getClassId();

		jobLevel = player.getClassLevel();
		int level = player.getLevel();

    boolean hasDualClass = false;

    for( SubClass s : player.getSubClassList().values() )
    {
      if( s.isDouble() )
      {
        hasDualClass = true;
        break;
      }
    }

    StringBuilder html = new StringBuilder();
    html.append( "<br>" );
    html.append("<table width=600>");
		html.append("<tr><td>");

		if(Config.ALLOW_CLASS_MASTERS_LIST.isEmpty() || !Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
		{
			jobLevel = 5;
		}
    if( (level >= 20 && jobLevel == 1 || level >= 40 && jobLevel == 2 || level >= 76 && jobLevel == 3 || level >= 85 && jobLevel == 4) && Config.ALLOW_CLASS_MASTERS_LIST.contains( jobLevel ) )
    {
			ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.CLASS_MASTERS_PRICE_ITEM_LIST[jobLevel]);
			html.append("Вы должны заплатить: <font color=\"LEVEL\">");
			html.append(Config.CLASS_MASTERS_PRICE_LIST[jobLevel] + "</font> <font color=\"LEVEL\">" + item.getName() + "</font> для смены профессии<br>");
			html.append("<center><table width=600><tr>");
			for(ClassId cid : ClassId.values())
			{
				if(cid == ClassId.INSPECTOR)
				{
					continue;
				}
				if(cid.childOf(classId) && cid.getClassLevel().ordinal() == classId.getClassLevel().ordinal() + 1)
				{
					html.append("<td><center><button value=\"" + cid.name() + "\" action=\"bypass _bbsclass_change " + cid.getId() + " " + Config.CLASS_MASTERS_PRICE_LIST[jobLevel] + "\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center></td>");
				}
			}
			html.append("</tr>");
			html.append("<tr><td><center><button value=\"Дуал-Класс\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center></td></tr>");
			html.append("</table></center>");
			html.append("</td>");
			html.append("</tr>");
			html.append("</table>");
		}
		else
		{
			switch(jobLevel)
			{
				case 1:
					html.append("Приветствую, <font color=F2C202>" + player.getName() + "</font> . Ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для того, что бы сменить вашу профессию, вы должны достичь: <font color=F2C202>20-го уровня.</font><br>");
					html.append("Для активации сабклассов вы должны достичь <font color=F2C202>76-го уровня.</font><br>");
					html.append("Что бы стать дворянином, вы должны прокачать сабкласс до <font color=F2C202>76-го уровня.</font><br>");
          if( !hasDualClass )
          {
            html.append( "<center><button value=\"Дуал-Класс\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>" );
          }
          else
          {
            html.append( "Вы уже имеете дуал-класс. Получение дуал-класса более не доступно.<br>" );
          }
          break;
        case 2:
					html.append("Приветствую, <font color=F2C202>" + player.getName() + "</font> . Ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для того, что бы сменить вашу профессию, вы должны достичь: <font color=F2C202>40-го уровня.</font><br>");
					html.append("Для активации сабклассов вы должны достичь <font color=F2C202>76-го уровня.</font><br>");
					html.append("Что бы стать дворянином, вы должны прокачать сабкласс до <font color=F2C202>76-го уровня.</font><br>");
          if( !hasDualClass )
          {
            html.append( "<center><button value=\"Дуал-Класс\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>" );
          }
          else
          {
            html.append( "Вы уже имеете дуал-класс. Получение дуал-класса более не доступно.<br>" );
          }
          break;
        case 3:
					html.append("Приветствую, <font color=F2C202>" + player.getName() + "</font> . Ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для того, что бы сменить вашу профессию, вы должны достичь: <font color=F2C202>76-го уровня.</font><br>");
					html.append("Для активации сабклассов вы должны достичь <font color=F2C202>76-го уровня.</font><br>");
					html.append("Что бы стать дворянином, вы должны прокачать сабкласс до <font color=F2C202>76-го уровня</font><br>");
          if( !hasDualClass )
          {
            html.append( "<center><button value=\"Дуал-Класс\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>" );
          }
          else
          {
            html.append( "Вы уже имеете дуал-класс. Получение дуал-класса более не доступно.<br>" );
          }
          break;
        case 4:
          html.append( "Приветствую, <font color=F2C202>" + player.getName() + "</font> . Ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>" );
          html.append( "Для того, что бы сменить вашу профессию, вы должны достичь: <font color=F2C202>85-го уровня.</font><br>" );
          html.append( "Для активации сабклассов вы должны достичь <font color=F2C202>85-го уровня.</font><br>" );
          html.append( "Что бы стать дворянином, вы должны прокачать сабкласс до <font color=F2C202>85-го уровня</font><br>" );
          if( !hasDualClass )
          {
            html.append( "<center><button value=\"Дуал-Класс\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>" );
          }
          else
          {
            html.append( "Вы уже имеете дуал-класс. Получение дуал-класса более не доступно.<br>" );
          }
          break;
        case 5:
          html.append( "Приветствую, <font color=F2C202>" + player.getName() + "</font> . Ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>" );
          html.append( "Для вас больше нет доступных профессий, либо Класс-мастер в данный момент не доступен.<br>" );
          if( level < 76 )
          {
            break;
          }
          html.append( "Вы достигли <font color=F2C202>76-го уровня</font>, активация сабклассов теперь доступна.<br>" );
          if( !player.isNoble() )
          {
            html.append( "Вы можете получить дворянство. Посетите раздел 'Магазин'.<br>" );
          }
          else
          {
            html.append( "Вы уже дворянин. Получение дворянства более не доступно.<br>" );
          }
          if( !hasDualClass )
          {
            html.append( "<center><button value=\"Дуал-Класс\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>" );
          }
          else
          {
            html.append( "Вы уже имеете дуал-класс. Получение дуал-класса более не доступно.<br>" );
          }
          break;
      }
		}
		return html.toString();
	}

	private void changeClass(Player player, int val)
	{
    if( player.getClassId().isOfLevel( ClassLevel.Third ) || player.getClassId().isOfLevel( ClassLevel.Fourth ) )
    {
			player.sendPacket(Msg.YOU_HAVE_COMPLETED_THE_QUEST_FOR_3RD_OCCUPATION_CHANGE_AND_MOVED_TO_ANOTHER_CLASS_CONGRATULATIONS); // для 3 профы
		}
		else
		{
			player.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS); // для 1 и 2 профы
		}

		player.setClassId(val, false, false);
		player.broadcastCharInfo();
	}
}