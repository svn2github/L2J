package services.community;

import l2next.gameserver.Config;
import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.data.xml.holder.ItemHolder;
import l2next.gameserver.handler.bbs.CommunityBoardHandler;
import l2next.gameserver.handler.bbs.ICommunityBoardHandler;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.Element;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.network.serverpackets.InventoryUpdate;
import l2next.gameserver.network.serverpackets.ShowBoard;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.templates.item.EtcItemTemplate;
import l2next.gameserver.templates.item.ItemTemplate;
import l2next.gameserver.utils.BbsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringTokenizer;

public class EnchantCommunity extends Functions implements ScriptFile, ICommunityBoardHandler
{
	private static EnchantCommunity _Instance = null;
	private static final Logger _log = LoggerFactory.getLogger(EnchantCommunity.class);

	public static EnchantCommunity getInstance()
	{
		if(_Instance == null)
		{
			_Instance = new EnchantCommunity();
		}
		return _Instance;
	}

	/**
	 * Имплементированые методы скриптов
	 */

	@Override
	public void onLoad()
	{
		if(Config.ENCHANT_ENABLED)
		{
			_log.info("CommunityBoard: Enchant Community service loaded.");
			CommunityBoardHandler.getInstance().registerHandler(this);
		}
	}

	@Override
	public void onReload()
	{
		if(Config.ENCHANT_ENABLED)
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
		return new String[]{"_bbsechant;"};
	}

	@Override
	public void onBypassCommand(Player activeChar, String command)
	{
		if(command.equals("_bbsechant;"))
		{
			String name = "None Name";
			name = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID).getName();
			StringBuilder sb = new StringBuilder();
			sb.append("<table width=400>");
			ItemInstance[] arr = activeChar.getInventory().getItems();
			int len = arr.length;
			for(int i = 0; i < len; i++)
			{
				ItemInstance _item = arr[i];
				if(_item == null || _item.getTemplate() instanceof EtcItemTemplate || /*_item.getTemplate().isShield() ||*/ _item.getTemplate().isBelt() || _item.getTemplate().isUnderwear() || !_item.isEquipped() || _item.isHeroWeapon() || _item.getTemplate().isBracelet() || _item.getTemplate().isCloak() || _item.getTemplate().getCrystalType() == ItemTemplate.Grade.NONE || _item.getItemId() >= 7816 && _item.getItemId() <= 7831 || _item.isShadowItem() || _item.isCommonItem() || _item.isTemporalItem() || _item.getEnchantLevel() >= (Config.MAX_ENCHANT + 1))
				{
					continue;
				}
				sb.append("<tr><td><img src=icon." + _item.getTemplate().getIcon() + " width=32 height=32></td><td>");
				sb.append("<font color=\"LEVEL\">" + _item.getTemplate().getName() + " " + (_item.getEnchantLevel() <= 0 ? "" : "</font><br><font color=3293F3>Заточено на: +" + _item.getEnchantLevel()) + "</font><br1>");

				sb.append("Заточка за: <font color=\"LEVEL\">" + name + "</font>");
				sb.append("<img src=\"l2ui.squaregray\" width=\"170\" height=\"1\">");
				sb.append("</td><td>");
				sb.append("<button value=\"Обычная\" action=\"bypass _bbsechant;enchlistpage;" + _item.getObjectId() + "\" width=75 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				sb.append("</td><td>");
				sb.append("<button value=\"Аттрибут\" action=\"bypass _bbsechant;enchlistpageAtrChus;" + _item.getObjectId() + "\" width=75 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				sb.append("</td></tr>");
			}

			sb.append("</table>");

			String content = HtmCache.getInstance().getNotNull("pages/enchanter.htm", activeChar);
			content = content.replace("%enchanter%", sb.toString());
			ShowBoard.separateAndSend(BbsUtil.htmlAll(content, activeChar), activeChar);
		}
		if(command.startsWith("_bbsechant;enchlistpage;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int ItemForEchantObjID = Integer.parseInt(st.nextToken());
			String name = "None Name";
			name = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID).getName();
			ItemInstance EhchantItem = activeChar.getInventory().getItemByObjectId(ItemForEchantObjID);

			StringBuilder sb = new StringBuilder();
			sb.append("Для обычной заточки выбрана вещь:<br1><table width=300>");
			sb.append("<tr><td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td><td width=236><center>");
			sb.append("<font color=\"LEVEL\">" + EhchantItem.getTemplate().getName() + " " + (EhchantItem.getEnchantLevel() <= 0 ? "" : "</font><br1><font color=3293F3>Заточено на: +" + EhchantItem.getEnchantLevel()) + "</font><br1>");

			sb.append("Заточка производится за: <font color=\"LEVEL\">" + name + "</font>");
			sb.append("<img src=\"l2ui.squaregray\" width=\"236\" height=\"1\"><center></td>");
			sb.append("<td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td>");
			sb.append("</tr>");
			sb.append("</table>");
			sb.append("<br>");
			sb.append("<br>");
			sb.append("<table border=0 width=400><tr><td width=200>");
			for(int i = 0; i < Config.ENCHANT_LEVELS.length; i++)
			{
				sb.append("<center><button value=\"На +" + Config.ENCHANT_LEVELS[i] + " (Цена:" + (EhchantItem.getTemplate().isWeapon() != false ? Config.ENCHANT_PRICE_WPN[i] : Config.ENCHANT_PRICE_ARM[i]) + " " + name + ")\" action=\"bypass _bbsechant;enchantgo;" + Config.ENCHANT_LEVELS[i] + ";" + (EhchantItem.getTemplate().isWeapon() != false ? Config.ENCHANT_PRICE_WPN[i] : Config.ENCHANT_PRICE_ARM[i]) + ";" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
				sb.append("<br1>");
			}

			sb.append("</td></tr></table><br1><button value=\"Назад\" action=\"bypass _bbsechant;\" width=70 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");

			String content = HtmCache.getInstance().getNotNull("pages/enchanter.htm", activeChar);
			content = content.replace("%enchanter%", sb.toString());
			ShowBoard.separateAndSend(BbsUtil.htmlAll(content, activeChar), activeChar);
		}
		if(command.startsWith("_bbsechant;enchlistpageAtrChus;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int ItemForEchantObjID = Integer.parseInt(st.nextToken());
			String name = "None Name";
			name = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID).getName();
			ItemInstance EhchantItem = activeChar.getInventory().getItemByObjectId(ItemForEchantObjID);

			StringBuilder sb = new StringBuilder();
			sb.append("Для заточки на атрибут выбрана вещь:<br><table width=300>");
			sb.append("<tr><td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td><td width=236><center>");
			sb.append("<font color=\"LEVEL\">" + EhchantItem.getTemplate().getName() + " " + (EhchantItem.getEnchantLevel() <= 0 ? "" : "</font><br1><font color=3293F3>Заточено на: +" + EhchantItem.getEnchantLevel()) + "</font><br1>");

			sb.append("Заточка производится за: <font color=\"LEVEL\">" + name + "</font>");
			sb.append("<img src=\"l2ui.squaregray\" width=\"236\" height=\"1\"><center></td>");
			sb.append("<td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td>");
			sb.append("</tr>");
			sb.append("</table>");
			sb.append("<br>");
			sb.append("<br>");
			sb.append("<table border=0 width=400><tr><td width=200>");
			sb.append("<center><img src=icon.etc_wind_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Wind \" action=\"bypass _bbsechant;enchlistpageAtr;2;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("<br><center><img src=icon.etc_earth_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Earth \" action=\"bypass _bbsechant;enchlistpageAtr;3;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("<br><center><img src=icon.etc_fire_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Fire \" action=\"bypass _bbsechant;enchlistpageAtr;0;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("</td><td width=200>");
			sb.append("<center><img src=icon.etc_water_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Water \" action=\"bypass _bbsechant;enchlistpageAtr;1;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("<br><center><img src=icon.etc_holy_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Divine \" action=\"bypass _bbsechant;enchlistpageAtr;4;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("<br><center><img src=icon.etc_unholy_stone_i00 width=32 height=32></center><br>");
			sb.append("<button value=\"Dark \" action=\"bypass _bbsechant;enchlistpageAtr;5;" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
			sb.append("</td></tr></table><br1><button value=\"Назад\" action=\"bypass _bbsechant;\" width=70 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
			String content = HtmCache.getInstance().getNotNull("pages/enchanter.htm", activeChar);
			content = content.replace("%enchanter%", sb.toString());
			ShowBoard.separateAndSend(BbsUtil.htmlAll(content, activeChar), activeChar);
		}
		if(command.startsWith("_bbsechant;enchlistpageAtr;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int AtributType = Integer.parseInt(st.nextToken());
			int ItemForEchantObjID = Integer.parseInt(st.nextToken());
			String ElementName = "";
			if(AtributType == 0)
			{
				ElementName = "Fire";
			}
			else if(AtributType == 1)
			{
				ElementName = "Water";
			}
			else if(AtributType == 2)
			{
				ElementName = "Wind";
			}
			else if(AtributType == 3)
			{
				ElementName = "Earth";
			}
			else if(AtributType == 4)
			{
				ElementName = "Divine";
			}
			else if(AtributType == 5)
			{
				ElementName = "Dark";
			}
			String name = "None Name";
			name = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID).getName();
			ItemInstance EhchantItem = activeChar.getInventory().getItemByObjectId(ItemForEchantObjID);
			StringBuilder sb = new StringBuilder();
			sb.append("Выбран элемент: <font color=\"LEVEL\">" + ElementName + "</font><br1> Для заточки выбрана вещь:<br1><table width=300>");
			sb.append("<tr><td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td><td width=236><center>");
			sb.append("<font color=\"LEVEL\">" + EhchantItem.getTemplate().getName() + " " + (EhchantItem.getEnchantLevel() <= 0 ? "" : "</font><br1><font color=3293F3>Заточено на: +" + EhchantItem.getEnchantLevel()) + "</font><br1>");

			sb.append("Заточка производится за: <font color=\"LEVEL\">" + name + "</font>");
			sb.append("<img src=\"l2ui.squaregray\" width=\"236\" height=\"1\"><center></td>");
			sb.append("<td width=32><img src=icon." + EhchantItem.getTemplate().getIcon() + " width=32 height=32> <img src=\"l2ui.squaregray\" width=\"32\" height=\"1\"></td>");
			sb.append("</tr>");
			sb.append("</table>");
			sb.append("<br1>");
			sb.append("<br1>");
			if(!EhchantItem.getTemplate().getName().contains("PvP") && (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S || EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S80 || EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S84 || EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R || EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R95 || EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R99))
			{
				sb.append("<table border=0 width=400><tr><td width=200>");
				for(int i = 0; i < (EhchantItem.getTemplate().isWeapon() != false ? Config.ENCHANT_ATTRIBUTE_LEVELS.length : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM.length); i++)
				{
					sb.append("<center><button value=\"На +" + (EhchantItem.getTemplate().isWeapon() != false ? Config.ENCHANT_ATTRIBUTE_LEVELS[i] : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM[i]) + " (Цена:" + (EhchantItem.getTemplate().isWeapon() != false ? Config.ATTRIBUTE_PRICE_WPN[i] : Config.ATTRIBUTE_PRICE_ARM[i]) + " " + name + ")\" action=\"bypass _bbsechant;enchantgoAtr;" + (EhchantItem.getTemplate().isWeapon() != false ? Config.ENCHANT_ATTRIBUTE_LEVELS[i] : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM[i]) + ";" + AtributType + ";" + (EhchantItem.getTemplate().isWeapon() != false ? Config.ATTRIBUTE_PRICE_WPN[i] : Config.ATTRIBUTE_PRICE_ARM[i]) + ";" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
					sb.append("<br1>");
				}
				sb.append("</td></tr></table><br1>");
			}
			else if(EhchantItem.getTemplate().getName().contains("PvP") && Config.ENCHANT_ATT_PVP && (EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S || EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S80 || EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.S84 || EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R || EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R95 || EhchantItem.getTemplate().getCrystalType() == ItemTemplate.Grade.R99))
			{
				sb.append("<table border=0 width=400><tr><td width=200>");
				for(int i = 0; i < (EhchantItem.getTemplate().isWeapon() != false ? Config.ENCHANT_ATTRIBUTE_LEVELS.length : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM.length); i++)
				{
					sb.append("<center><button value=\"На +" + (EhchantItem.getTemplate().isWeapon() != false ? Config.ENCHANT_ATTRIBUTE_LEVELS[i] : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM[i]) + " (Цена:" + (EhchantItem.getTemplate().isWeapon() != false ? Config.ATTRIBUTE_PRICE_WPN[i] : Config.ATTRIBUTE_PRICE_ARM[i]) + " " + name + ")\" action=\"bypass _bbsechant;enchantgoAtr;" + (EhchantItem.getTemplate().isWeapon() != false ? Config.ENCHANT_ATTRIBUTE_LEVELS[i] : Config.ENCHANT_ATTRIBUTE_LEVELS_ARM[i]) + ";" + AtributType + ";" + (EhchantItem.getTemplate().isWeapon() != false ? Config.ATTRIBUTE_PRICE_WPN[i] : Config.ATTRIBUTE_PRICE_ARM[i]) + ";" + ItemForEchantObjID + "\" width=200 height=20 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">");
					sb.append("<br1>");
				}
				sb.append("</td></tr></table><br1>");
			}
			else
			{
				sb.append("<table border=0 width=400><tr><td width=200>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("<center><font color=\"LEVEL\">Заточка данной вещи не возможна!</font></center>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("<br1>");
				sb.append("</td></tr></table><br>");
			}
			sb.append("<button value=\"Назад\" action=\"bypass _bbsechant;\" width=70 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");

			String content = HtmCache.getInstance().getNotNull("pages/enchanter.htm", activeChar);
			content = content.replace("%enchanter%", sb.toString());
			ShowBoard.separateAndSend(BbsUtil.htmlAll(content, activeChar), activeChar);
		}
		if(command.startsWith("_bbsechant;enchantgo;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int EchantVal = Integer.parseInt(st.nextToken());
			int EchantPrice = Integer.parseInt(st.nextToken());
			int EchantObjID = Integer.parseInt(st.nextToken());
			ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID);
			ItemInstance pay = activeChar.getInventory().getItemByItemId(item.getItemId());
			ItemInstance EhchantItem = activeChar.getInventory().getItemByObjectId(EchantObjID);
			if(pay != null && pay.getCount() >= EchantPrice)
			{
				activeChar.getInventory().destroyItem(pay, EchantPrice);
				if(EhchantItem.isEquipped())
				{
					activeChar.getInventory().unEquipItemInBodySlot(EhchantItem.getEquipSlot());
				}
				EhchantItem.setEnchantLevel(EchantVal);
				activeChar.getInventory().equipItem(EhchantItem);
				activeChar.sendPacket(new InventoryUpdate().addModifiedItem(EhchantItem));
				activeChar.broadcastUserInfo(true);
				activeChar.sendMessage("" + EhchantItem.getTemplate().getName() + " было заточено до " + EchantVal + ". Спасибо.");
				onBypassCommand(activeChar, "_bbsechant;");
			}
			else
			{
				activeChar.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
		}

		if(command.startsWith("_bbsechant;enchantgoAtr;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			int EchantVal = Integer.parseInt(st.nextToken());
			int AtrType = Integer.parseInt(st.nextToken());
			Element el = Element.getElementById(AtrType);
			int EchantPrice = Integer.parseInt(st.nextToken());
			int EchantObjID = Integer.parseInt(st.nextToken());
			ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.ENCHANTER_ITEM_ID);
			ItemInstance pay = activeChar.getInventory().getItemByItemId(item.getItemId());
			ItemInstance EhchantItem = activeChar.getInventory().getItemByObjectId(EchantObjID);
			if(EhchantItem.isWeapon())
			{
				if(pay != null && pay.getCount() >= EchantPrice)
				{
					activeChar.getInventory().destroyItem(pay, EchantPrice);
					if(EhchantItem.isEquipped())
					{
						activeChar.getInventory().unEquipItemInBodySlot(EhchantItem.getEquipSlot());
					}
					EhchantItem.setAttributeElement(el, EchantVal);
					activeChar.getInventory().equipItem(EhchantItem);
					activeChar.sendPacket(new InventoryUpdate().addModifiedItem(EhchantItem));
					activeChar.broadcastUserInfo(true);
					activeChar.sendMessage("" + EhchantItem.getTemplate().getName() + " было заточено до " + EchantVal + ". Спасибо.");
					onBypassCommand(activeChar, "_bbsechant;");
				}
				else
				{
					activeChar.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				}
			}
			else if(EhchantItem.isArmor())
			{
				if(!canEnchantArmorAttribute(AtrType, EhchantItem))
				{
					activeChar.sendMessage("Невозможно вставить аттрибут в броню, не соблюдены условия");
					onBypassCommand(activeChar, "_bbsechant;");
					return;
				}
				if(pay != null && pay.getCount() >= EchantPrice)
				{
					activeChar.getInventory().destroyItem(pay, EchantPrice);
					if(EhchantItem.isEquipped())
					{
						activeChar.getInventory().unEquipItemInBodySlot(EhchantItem.getEquipSlot());
					}
					EhchantItem.setAttributeElement(el, EchantVal);
					activeChar.getInventory().equipItem(EhchantItem);
					activeChar.sendPacket(new InventoryUpdate().addModifiedItem(EhchantItem));
					activeChar.broadcastUserInfo(true);
					activeChar.sendMessage("" + EhchantItem.getTemplate().getName() + " было заточено до " + EchantVal + ". Спасибо.");
					onBypassCommand(activeChar, "_bbsechant;");
				}
			}
		}
	}

	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
	}

	@SuppressWarnings("unused")
	private boolean canEnchantArmorAttribute(int attr, ItemInstance item)
	{
		Element elm = Element.getElementById(attr);
		switch(attr)
		{
			case 0:
				if(item.getDefenceWater() != 0)
				{
					return false;
				}
				break;
			case 1:
				if(item.getDefenceFire() != 0)
				{
					return false;
				}
				break;
			case 2:
				if(item.getDefenceEarth() != 0)
				{
					return false;
				}
				break;
			case 3:
				if(item.getDefenceWind() != 0)
				{
					return false;
				}
				break;
			case 4:
				if(item.getDefenceUnholy() != 0)
				{
					return false;
				}
				break;
			case 5:
				if(item.getDefenceHoly() != 0)
				{
					return false;
				}
				break;
		}
		return true;
	}
}