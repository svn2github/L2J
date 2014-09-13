package l2next.gameserver.data.xml.parser;

import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.ItemHolder;
import l2next.gameserver.data.xml.holder.OptionDataHolder;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.base.Element;
import l2next.gameserver.model.items.CrystallizationItem;
import l2next.gameserver.stats.conditions.Condition;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.templates.OptionDataTemplate;
import l2next.gameserver.templates.StatsSet;
import l2next.gameserver.templates.item.ArmorTemplate;
import l2next.gameserver.templates.item.Bodypart;
import l2next.gameserver.templates.item.ItemTemplate;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author VISTALL
 * @date 11:26/15.01.2011
 */
public final class ArmorItemParser extends StatParser<ItemHolder>
{
	private static final ArmorItemParser _instance = new ArmorItemParser();

	public static ArmorItemParser getInstance()
	{
		return _instance;
	}

	protected ArmorItemParser()
	{
		super(ItemHolder.getInstance());
	}

	@Override
	public ArrayList<File> getXMLDir()
	{
		ArrayList<File> files = new ArrayList<>();
		files.add(new File(Config.DATAPACK_ROOT, "data/stats/items/armor/"));
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
		return "item.dtd";
	}

	@Override
	protected void readData(org.dom4j.Element rootElement) throws Exception
	{
		for(Iterator<org.dom4j.Element> itemIterator = rootElement.elementIterator(); itemIterator.hasNext(); )
		{
			org.dom4j.Element itemElement = itemIterator.next();
			StatsSet set = new StatsSet();
			set.set("item_id", itemElement.attributeValue("id"));
			set.set("name", itemElement.attributeValue("name"));
			set.set("add_name", itemElement.attributeValue("add_name", StringUtils.EMPTY));

			int slot = 0;
			for(Iterator<org.dom4j.Element> subIterator = itemElement.elementIterator(); subIterator.hasNext(); )
			{
				org.dom4j.Element subElement = subIterator.next();
				String subName = subElement.getName();
				if(subName.equalsIgnoreCase("set"))
				{
					set.set(subElement.attributeValue("name"), subElement.attributeValue("value"));
				}
				else if(subName.equalsIgnoreCase("equip"))
				{
					for(Iterator<org.dom4j.Element> slotIterator = subElement.elementIterator(); slotIterator.hasNext(); )
					{
						org.dom4j.Element slotElement = slotIterator.next();
						Bodypart bodypart = Bodypart.valueOf(slotElement.attributeValue("id"));
						if(bodypart.getReal() != null)
						{
							slot = bodypart.mask();
						}
						else
						{
							slot |= bodypart.mask();
						}
					}
				}
			}

			set.set("bodypart", slot);

			ItemTemplate template = null;
			try
			{
				if(itemElement.getName().equalsIgnoreCase("armor"))
				{
					if(!set.containsKey("class"))
					{
						if((slot & ItemTemplate.SLOTS_ARMOR) > 0)
						{
							set.set("class", ItemTemplate.ItemClass.ARMOR);
						}
						else if((slot & ItemTemplate.SLOTS_JEWELRY) > 0)
						{
							set.set("class", ItemTemplate.ItemClass.JEWELRY);
						}
						else
						{
							set.set("class", ItemTemplate.ItemClass.ACCESSORY);
						}
					}
					template = new ArmorTemplate(set);
				}
			}
			catch(Exception e)
			{
				// for(Map.Entry<String, Object> entry : set.entrySet())
				// {
				// info("set " + entry.getKey() + ":" + entry.getValue());
				// }
				warn("Fail create item: " + set.get("item_id"), e);
				continue;
			}

			for(Iterator<org.dom4j.Element> subIterator = itemElement.elementIterator(); subIterator.hasNext(); )
			{
				org.dom4j.Element subElement = subIterator.next();
				String subName = subElement.getName();
				if(subName.equalsIgnoreCase("for"))
				{
					parseFor(subElement, template);
				}
				else if(subName.equalsIgnoreCase("triggers"))
				{
					parseTriggers(subElement, template);
				}
				else if(subName.equalsIgnoreCase("skills"))
				{
					for(Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext(); )
					{
						org.dom4j.Element nextElement = nextIterator.next();
						int id = Integer.parseInt(nextElement.attributeValue("id"));
						int level = Integer.parseInt(nextElement.attributeValue("level"));

						Skill skill = SkillTable.getInstance().getInfo(id, level);

						if(skill != null)
						{
							template.attachSkill(skill);
						}
						else
						{
							info("Skill not found(" + id + "," + level + ") for item:" + set.getObject("item_id") + "; file:" + getCurrentFileName());
						}
					}
				}
				else if(subName.equalsIgnoreCase("enchant4_skill"))
				{
					int id = Integer.parseInt(subElement.attributeValue("id"));
					int level = Integer.parseInt(subElement.attributeValue("level"));

					Skill skill = SkillTable.getInstance().getInfo(id, level);
					if(skill != null)
					{
						template.setEnchant4Skill(skill);
					}
				}
				else if(subName.equalsIgnoreCase("cond"))
				{
					Condition condition = parseFirstCond(subElement);
					if(condition != null)
					{
						int msgId = parseNumber(subElement.attributeValue("msgId")).intValue();
						condition.setSystemMsg(msgId);

						template.setCondition(condition);
					}
				}
				else if(subName.equalsIgnoreCase("attributes"))
				{
					int[] attributes = new int[6];
					for(Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext(); )
					{
						org.dom4j.Element nextElement = nextIterator.next();
						Element element;
						if(nextElement.getName().equalsIgnoreCase("attribute"))
						{
							element = Element.getElementByName(nextElement.attributeValue("element"));
							attributes[element.getId()] = Integer.parseInt(nextElement.attributeValue("value"));
						}
					}
					template.setBaseAtributeElements(attributes);
				}
				else if(subName.equalsIgnoreCase("crystallization_bonus"))
				{
					for(Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext(); )
					{
						org.dom4j.Element nextElement = nextIterator.next();
						final int id = Integer.parseInt(nextElement.attributeValue("id"));
						final long count = Long.parseLong(nextElement.attributeValue("count"));
						double chance = Double.parseDouble(nextElement.attributeValue("chance"));
						final CrystallizationItem exa = new CrystallizationItem(id, count, chance);

						template.addExCrystallization(exa);
					}
				}
				else if(subName.equalsIgnoreCase("enchant_options"))
				{
					for(Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext(); )
					{
						org.dom4j.Element nextElement = nextIterator.next();

						if(nextElement.getName().equalsIgnoreCase("level"))
						{
							int val = Integer.parseInt(nextElement.attributeValue("val"));

							int i = 0;
							int[] options = new int[3];
							for(org.dom4j.Element optionElement : nextElement.elements())
							{
								OptionDataTemplate optionData = OptionDataHolder.getInstance().getTemplate(Integer.parseInt(optionElement.attributeValue("id")));
								if(optionData == null)
								{
									error("Not found option_data for id: " + optionElement.attributeValue("id") + "; item_id: " + set.get("item_id"));
									continue;
								}
								options[i++] = optionData.getId();
							}
							template.addEnchantOptions(val, options);
						}
					}
				}
			}
			getHolder().addItem(template);
		}
	}

	@Override
	protected Object getTableValue(String name)
	{
		return null;
	}
}
