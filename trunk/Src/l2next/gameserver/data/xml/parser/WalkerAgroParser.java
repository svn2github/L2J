package l2next.gameserver.data.xml.parser;

import l2next.commons.data.xml.AbstractFileParser;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.WalkerAgroHolder;
import l2next.gameserver.templates.spawn.WalkerAgroTemplate;
import l2next.gameserver.templates.spawn.WalkerAgroTemplate.RouteType;
import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;

public final class WalkerAgroParser extends AbstractFileParser<WalkerAgroHolder>
{
	private static final WalkerAgroParser _instance = new WalkerAgroParser();

	public static WalkerAgroParser getInstance()
	{
		return _instance;
	}

	protected WalkerAgroParser()
	{
		super(WalkerAgroHolder.getInstance());
	}

	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/routes/walker_agro.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "walker_agro.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> routeIterator = rootElement.elementIterator(); routeIterator.hasNext(); )
		{
			Element routeElement = routeIterator.next();
			if(routeElement.getName().equalsIgnoreCase("route"))
			{
				int npcId = Integer.parseInt(routeElement.attributeValue("npcId"));
				RouteType type = RouteType.valueOf(routeElement.attributeValue("type"));
				long baseDelay = Long.parseLong(routeElement.attributeValue("delay"));
				boolean isRunning = Boolean.parseBoolean(routeElement.attributeValue("isRunning"));
				int walkRange = Integer.parseInt(routeElement.attributeValue("walkRange"));

				WalkerAgroTemplate template = new WalkerAgroTemplate(npcId, baseDelay, type, isRunning, walkRange);

				for(Iterator<Element> subIterator = routeElement.elementIterator(); subIterator.hasNext(); )
				{
					Element subElement = subIterator.next();
					if(subElement.getName().equalsIgnoreCase("point"))
					{
						int x = Integer.parseInt(subElement.attributeValue("x"));
						int y = Integer.parseInt(subElement.attributeValue("y"));
						int z = Integer.parseInt(subElement.attributeValue("z"));
						int h = subElement.attributeValue("h") == null ? -1 : Integer.parseInt(subElement.attributeValue("h"));
						long delay = subElement.attributeValue("delay") == null ? 0 : Long.parseLong(subElement.attributeValue("delay"));
						boolean end = subElement.attributeValue("endPoint") != null && Boolean.parseBoolean(subElement.attributeValue("endPoint"));
						// String npcString =
						// subElement.attributeValue("npcString"); - for
						// npcString Message,

						template.setRoute(x, y, z, h, delay, end);
					}
				}

				getHolder().addSpawn(template);
			}
		}
	}
}
