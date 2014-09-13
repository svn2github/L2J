package l2next.gameserver.data.xml.parser;

import l2next.commons.data.xml.AbstractFileParser;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.StatuesHolder;
import l2next.gameserver.model.worldstatistics.CategoryType;
import l2next.gameserver.utils.Location;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Дмитрий
 * @modified KilRoy
 * @date 17.10.12 9:39
 */
public class StatuesSpawnParser extends AbstractFileParser<StatuesHolder>
{
	private static StatuesSpawnParser ourInstance = new StatuesSpawnParser();

	private StatuesSpawnParser()
	{
		super(StatuesHolder.getInstance());
	}

	public static StatuesSpawnParser getInstance()
	{
		return ourInstance;
	}

	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/StatuesSpawnData.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "StatuesSpawnData.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{

		for(Element statuesElement : rootElement.elements())
		{
			int type = Integer.parseInt(statuesElement.attributeValue("type"));
			CategoryType categoryType = CategoryType.getCategoryById(type, 0);

			//List<Location> locations = new ArrayList<>();
			List<Location> locations = new ArrayList<Location>();
			for(Element spawnElement : statuesElement.elements())
			{
				String[] loc = spawnElement.attributeValue("loc").split(",");
				locations.add(new Location(Integer.parseInt(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3])));
			}
			StatuesHolder.getInstance().addSpawnInfo(categoryType, locations);
		}
	}
}
