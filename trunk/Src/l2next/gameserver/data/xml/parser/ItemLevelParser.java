package l2next.gameserver.data.xml.parser;

import l2next.commons.data.xml.AbstractFileParser;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.ItemLevelHolder;
import org.dom4j.Element;

import java.io.File;

public class ItemLevelParser extends AbstractFileParser<ItemLevelHolder>
{
	private static ItemLevelParser _instance = new ItemLevelParser();

	int levelPlayer;

	protected ItemLevelParser()
	{
		super(ItemLevelHolder.getInstance());
	}

	public static ItemLevelParser getInstance()
	{
		return _instance;
	}

	public String getDTDFileName()
	{
		return "items_on_level.dtd";
	}

	protected void readData(Element rootElement) throws Exception
	{
		ItemLevelHolder.ItemLevel template = null;
		for(Element equipmentElement : rootElement.elements())
		{
			levelPlayer = Integer.parseInt(equipmentElement.attributeValue("level"));
			for(Element item : equipmentElement.elements())
			{
				template = new ItemLevelHolder.ItemLevel();
				try
				{
					template.id = Integer.parseInt(item.attributeValue("id"));
					template.count = Integer.parseInt(item.attributeValue("count"));

					getHolder().addItemLevel(template, levelPlayer);
				}
				catch(NumberFormatException e)
				{
					_log.warn("Cold not load item: " + e.getMessage());
				}
			}
		}
	}

	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/stats/player/items_on_level.xml");
	}
}