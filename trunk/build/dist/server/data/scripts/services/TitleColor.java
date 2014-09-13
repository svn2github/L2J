package services;

import l2next.gameserver.Config;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.data.xml.holder.ItemHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.PcInventory;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.templates.item.ItemTemplate;

public class TitleColor
  extends Functions
{
  public void list()
  {
    Player player = getSelf();
    if (player == null) {
      return;
    }
    if (!Config.SERVICES_CHANGE_TITLE_COLOR_ENABLED)
    {
      show("Сервис отключен.", player);
      return;
    }
    StringBuilder append = new StringBuilder();
    append.append("You can change title color for small price: ").append(Config.SERVICES_CHANGE_TITLE_COLOR_PRICE).append(" ").append(ItemHolder.getInstance().getTemplate(Config.SERVICES_CHANGE_TITLE_COLOR_ITEM).getName()).append(".");
    append.append("<br>Possible colors:<br>");
    for (String color : Config.SERVICES_CHANGE_TITLE_COLOR_LIST) {
      append.append("<br><a action=\"bypass -h scripts_services.TitleColor:change ").append(color).append("\"><font color=\"").append(color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2)).append("\">").append(color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2)).append("</font></a>");
    }
    append.append("<br><a action=\"bypass -h scripts_services.TitleColor:change FFFFFF\"><font color=\"FFFFFF\">Revert to default (Free)</font></a>");
    show(append.toString(), player, null, new Object[0]);
  }
  
  public void change(String[] param)
  {
    Player player = getSelf();
    if (player == null) {
      return;
    }
    if (!Config.SERVICES_CHANGE_TITLE_COLOR_ENABLED)
    {
      show("Сервис отключен.", player);
      return;
    }
    if (param[0].equalsIgnoreCase("FFFFFF"))
    {
      player.setTitleColor(Integer.decode("0xFFFFFF").intValue());
      player.broadcastUserInfo(true);
      return;
    }
    if (player.getInventory().destroyItemByItemId(Config.SERVICES_CHANGE_TITLE_COLOR_ITEM, Config.SERVICES_CHANGE_TITLE_COLOR_PRICE))
    {
      player.setTitleColor(Integer.decode("0x" + param[0]).intValue());
      player.broadcastUserInfo(true);
    }
    else if (Config.SERVICES_CHANGE_TITLE_COLOR_ITEM == 57)
    {
      player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
    }
    else
    {
      player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
    }
  }
}
