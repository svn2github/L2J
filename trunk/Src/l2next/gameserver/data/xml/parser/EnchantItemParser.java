package l2next.gameserver.data.xml.parser;

import l2next.commons.data.xml.AbstractDirParser;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.EnchantItemHolder;
import l2next.gameserver.model.items.etcitems.AppearanceStone;
import l2next.gameserver.model.items.etcitems.AttributeStone;
import l2next.gameserver.model.items.etcitems.EnchantScroll;
import l2next.gameserver.model.items.etcitems.LifeStone;
import l2next.gameserver.templates.item.ExItemType;
import l2next.gameserver.templates.item.ItemTemplate;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @user: Mifesto
 * @date: 11:01 / 03.05.13
 * @team: http://novell-project.ru/
 */
public class EnchantItemParser extends AbstractDirParser<EnchantItemHolder>
{
	private static EnchantItemParser _instance = new EnchantItemParser();

	private EnchantItemParser()
	{
		super(EnchantItemHolder.getInstance());
	}

	public static EnchantItemParser getInstance()
	{
		return _instance;
	}

	@Override
	public ArrayList<File> getXMLDir()
	{
		ArrayList<File> files = new ArrayList<>();
		files.add(new File(Config.DATAPACK_ROOT, "data/stats/items/enchant/"));
		return files;
	}

	@Override
	public boolean isIgnored(File f)
	{
		return false;
	}

	@Override
	public String getDTDFileName()
	{
		return "enchant_items.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Element enchantItemElement : rootElement.elements("enchant_scroll"))
		{
			EnchantScroll enchantScroll = new EnchantScroll();

			enchantScroll.setItemId(Integer.parseInt(enchantItemElement.attributeValue("id")));
			enchantScroll.setType(EnchantScroll.ScrollType.valueOf(enchantItemElement.attributeValue("type").toUpperCase()));
			enchantScroll.setTarget(EnchantScroll.ScrollTarget.valueOf(enchantItemElement.attributeValue("target").toUpperCase()));
			enchantScroll.setGrade(ItemTemplate.Grade.valueOf(enchantItemElement.attributeValue("grade").toUpperCase()));
			enchantScroll.setChance(Integer.parseInt(enchantItemElement.attributeValue("chance")));
			enchantScroll.setMin(Integer.parseInt(enchantItemElement.attributeValue("min")));
			enchantScroll.setSafe(Integer.parseInt(enchantItemElement.attributeValue("safe")));
			enchantScroll.setMax(Integer.parseInt(enchantItemElement.attributeValue("max")));

			getHolder().addEnchantScroll(enchantScroll);

			Element itemElement = enchantItemElement.element("item_list");
			if(itemElement != null)
			{
				for(Element e : itemElement.elements())
				{
					enchantScroll.addTargetItems(Integer.parseInt(e.attributeValue("id")));
				}
			}
		}
		for(Iterator<Element> iterator = rootElement.elementIterator("appearance_stone"); iterator.hasNext(); )
		{
			Element stoneElement = iterator.next();
			int itemId = Integer.parseInt(stoneElement.attributeValue("id"));

			String[] targetTypesStr = stoneElement.attributeValue("target_type").split(",");
			AppearanceStone.ShapeTargetType[] targetTypes = new AppearanceStone.ShapeTargetType[targetTypesStr.length];
			for(int i = 0; i < targetTypesStr.length; i++)
			{
				targetTypes[i] = AppearanceStone.ShapeTargetType.valueOf(targetTypesStr[i].toUpperCase());
			}

			AppearanceStone.ShapeType type = AppearanceStone.ShapeType.valueOf(stoneElement.attributeValue("shifting_type").toUpperCase());

			String[] gradesStr = stoneElement.attributeValue("grade") == null ? new String[0] : stoneElement.attributeValue("grade").split(",");
			ItemTemplate.Grade[] grades = new ItemTemplate.Grade[gradesStr.length];
			for(int i = 0; i < gradesStr.length; i++)
			{
				grades[i] = ItemTemplate.Grade.valueOf(gradesStr[i].toUpperCase());
			}

			long cost = stoneElement.attributeValue("cost") == null ? 0L : Long.parseLong(stoneElement.attributeValue("cost"));
			int extractItemId = stoneElement.attributeValue("extract_id") == null ? 0 : Integer.parseInt(stoneElement.attributeValue("extract_id"));

			String[] itemTypesStr = stoneElement.attributeValue("item_type") == null ? new String[0] : stoneElement.attributeValue("item_type").split(",");
			ExItemType[] itemTypes = new ExItemType[itemTypesStr.length];
			for(int i = 0; i < itemTypesStr.length; i++)
			{
				itemTypes[i] = ExItemType.valueOf(itemTypesStr[i].toUpperCase());
			}

			getHolder().addAppearanceStone(new AppearanceStone(itemId, targetTypes, type, grades, cost, extractItemId, itemTypes));
		}
		for(Element enchantItemElement : rootElement.elements("attribute_stone"))
		{
			AttributeStone attributeStone = new AttributeStone();

			attributeStone.setItemId(Integer.parseInt(enchantItemElement.attributeValue("id")));
			attributeStone.setMinArmor(Integer.parseInt(enchantItemElement.attributeValue("min_arm")));
			attributeStone.setMaxArmor(Integer.parseInt(enchantItemElement.attributeValue("max_arm")));
			attributeStone.setMinWeapon(Integer.parseInt(enchantItemElement.attributeValue("min_weap")));
			attributeStone.setMaxWeapon(Integer.parseInt(enchantItemElement.attributeValue("max_weap")));
			attributeStone.setIncArmor(Integer.parseInt(enchantItemElement.attributeValue("inc_arm")));
			attributeStone.setIncWeapon(Integer.parseInt(enchantItemElement.attributeValue("inc_weap")));
			attributeStone.setInсWeaponArmor(Integer.parseInt(enchantItemElement.attributeValue("inc_weap_arm")));
			attributeStone.setChance(Integer.parseInt(enchantItemElement.attributeValue("chance")));
			attributeStone.setElement(l2next.gameserver.model.base.Element.valueOf(enchantItemElement.attributeValue("element", "NONE")));

			getHolder().addAttributeStone(attributeStone);
		}
		for(Element enchantItemElement : rootElement.elements("life_stone"))
		{
			LifeStone lifeStone = new LifeStone();

			lifeStone.setItemId(Integer.parseInt(enchantItemElement.attributeValue("id")));
			lifeStone.setLevel(Integer.parseInt(enchantItemElement.attributeValue("level")));
			lifeStone.setGrade(LifeStone.StoneGrade.valueOf(enchantItemElement.attributeValue("grade").toUpperCase()));

			getHolder().addLifeStone(lifeStone);
		}
	}
}