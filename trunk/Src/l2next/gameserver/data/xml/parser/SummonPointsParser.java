package l2next.gameserver.data.xml.parser;

import l2next.commons.data.xml.AbstractFileParser;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.SummonPointsHolder;
import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;

/**
 * @author ALF
 * @date 09.08.2012
 */
public class SummonPointsParser extends AbstractFileParser<SummonPointsHolder>
{
	private static SummonPointsParser _instance = new SummonPointsParser();

	public static SummonPointsParser getInstance()
	{
		return _instance;
	}

	private SummonPointsParser()
	{
		super(SummonPointsHolder.getInstance());
	}

	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/SummonPointsData.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "SummonPointsData.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); )
		{
			Element pointData = iterator.next();

			int npcId = Integer.parseInt(pointData.attributeValue("id"));
			int point = Integer.parseInt(pointData.attributeValue("points"));

			getHolder().addSummonPoints(npcId, point);
		}
	}
}
