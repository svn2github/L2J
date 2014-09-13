package l2next.gameserver.data.xml.parser;

import l2next.commons.collections.MultiValueSet;
import l2next.commons.data.xml.AbstractDirParser;
import l2next.commons.geometry.Polygon;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.SpawnHolder;
import l2next.gameserver.model.Territory;
import l2next.gameserver.templates.StatsSet;
import l2next.gameserver.templates.spawn.PeriodOfDay;
import l2next.gameserver.templates.spawn.SpawnNpcInfo;
import l2next.gameserver.templates.spawn.SpawnTemplate;
import l2next.gameserver.utils.Location;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Awakeninger
 * @date 16:45/1.03.2013
 */
public final class SpawnsData extends AbstractDirParser<SpawnHolder>
{
	private static final SpawnsData _instance = new SpawnsData();

	private static final Logger _log = LoggerFactory.getLogger(SpawnsData.class);

	public static SpawnsData getInstance()
	{
		return _instance;
	}

	protected SpawnsData()
	{
		super(SpawnHolder.getInstance());
	}

	@Override
	public ArrayList<File> getXMLDir()
	{
		ArrayList<File> files = new ArrayList<>();
		files.add(new File(Config.DATAPACK_ROOT, "data/stats/npc/spawnlist/"));
		//files.add(new File(Config.DATAPACK_ROOT, "custom/spawnlist/"));
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
		return "spawn.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> spawnIterator = rootElement.elementIterator(); spawnIterator.hasNext(); )
		{
			Element spawnElement = spawnIterator.next();
			if(spawnElement.getName().equalsIgnoreCase("spawns"))
			{
				String name = spawnElement.attributeValue("name");
				for(Iterator<Element> subIterator = spawnElement.elementIterator(); subIterator.hasNext(); )
				{
					Element subElement = subIterator.next();
					if(subElement.getName().equalsIgnoreCase("spawn"))
					{

						int npcId = Integer.parseInt(subElement.attributeValue("npcId"));
						int count = 1;
						int x = Integer.parseInt(subElement.attributeValue("x"));
						int y = Integer.parseInt(subElement.attributeValue("y"));
						int z = Integer.parseInt(subElement.attributeValue("z"));
						int h = Integer.parseInt(subElement.attributeValue("heading"));
						int respawn = 60;

						if(subElement.attributeValue("delay") != null)
						{
							respawn = Integer.parseInt(subElement.attributeValue("delay"));
						}

						int respawnRandom = 0;

						int periodODay = 0;
						if(subElement.attributeValue("periodOfDay") != null)
						{
							periodODay = Integer.parseInt(subElement.attributeValue("periodOfDay"));
						}

						PeriodOfDay periodOfDay = periodODay == 0 ? PeriodOfDay.NONE : periodODay == 1 ? PeriodOfDay.DAY : PeriodOfDay.NIGHT;
						String group = periodOfDay.name();
						SpawnTemplate template = new SpawnTemplate(periodOfDay, count, respawn, respawnRandom);
						template.addSpawnRange(new Location(x, y, z, h));

						MultiValueSet<String> parameters = StatsSet.EMPTY;
						if(parameters.isEmpty())
						{
							parameters = new MultiValueSet<String>();
						}

						template.addNpc(new SpawnNpcInfo(npcId, 0, parameters));

						if(template.getNpcSize() == 0)
						{
							_log.info(npcId + "");
							_log.info("Npc id is zero! File: " + getCurrentFileName());
							continue;
						}

						if(template.getSpawnRangeSize() == 0)
						{
							_log.info("No points to spawn! File: " + getCurrentFileName());
							continue;
						}

						getHolder().addSpawn(group, template);

					}
				}
			}
		}
	}

	private Territory parseTerritory(String name, Element e)
	{
		Territory t = new Territory();
		t.add(parsePolygon0(name, e));

		for(Iterator<Element> iterator = e.elementIterator("banned_territory"); iterator.hasNext(); )
		{
			t.addBanned(parsePolygon0(name, iterator.next()));
		}

		return t;
	}

	private Polygon parsePolygon0(String name, Element e)
	{
		Polygon temp = new Polygon();
		for(Iterator<Element> addIterator = e.elementIterator("add"); addIterator.hasNext(); )
		{
			Element addElement = addIterator.next();
			int x = Integer.parseInt(addElement.attributeValue("x"));
			int y = Integer.parseInt(addElement.attributeValue("y"));
			int zmin = Integer.parseInt(addElement.attributeValue("zmin"));
			int zmax = Integer.parseInt(addElement.attributeValue("zmax"));
			temp.add(x, y).setZmin(zmin).setZmax(zmax);
		}

		if(!temp.validate())
		{
			error("Invalid polygon: " + name + "{" + temp + "}. File: " + getCurrentFileName());
		}
		return temp;
	}
}
