package l2next.gameserver.data.xml.parser;

import l2next.commons.data.xml.AbstractFileParser;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.StartItemHolder;
import org.dom4j.Element;

import java.io.File;

public class StartItemParser extends AbstractFileParser<StartItemHolder>
{
	private static StartItemParser _instance = new StartItemParser();

	int classId;

	protected StartItemParser()
	{
		super(StartItemHolder.getInstance());
	}

	public static StartItemParser getInstance()
	{
		return _instance;
	}

	public String getDTDFileName()
	{
		return "start_items.dtd";
	}

	protected void readData(Element rootElement) throws Exception
	{
		StartItemHolder.StartItem template = null;
		for(Element equipmentElement : rootElement.elements())
		{
			classId = Integer.parseInt(equipmentElement.attributeValue("classId"));
			for(Element item : equipmentElement.elements())
			{
				template = new StartItemHolder.StartItem();
				try
				{
					template.id = Integer.parseInt(item.attributeValue("id"));
					template.count = Integer.parseInt(item.attributeValue("count"));
					template.equipped = Boolean.parseBoolean(item.attributeValue("equipped", "false"));
					template.warehouse = Boolean.parseBoolean(item.attributeValue("warehouse", "false"));

					getHolder().addStartItem(template, classId);
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
		return new File(Config.DATAPACK_ROOT, "data/stats/player/start_items.xml");
	}
}