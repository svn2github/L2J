package l2next.gameserver.data.xml.parser;

import l2next.commons.data.xml.AbstractDirParser;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.SkillAcquireHolder;
import l2next.gameserver.model.SkillLearn;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author: VISTALL
 * @date: 20:55/30.11.2010
 * @Correct: JustForFun
 * @date: 01:18/02.04.2013
 */

public final class SkillAcquireParser extends AbstractDirParser<SkillAcquireHolder>
{
	private static final SkillAcquireParser _instance = new SkillAcquireParser();

	public static SkillAcquireParser getInstance()
	{
		return _instance;
	}

	protected SkillAcquireParser()
	{
		super(SkillAcquireHolder.getInstance());
	}

	@Override
	public ArrayList<File> getXMLDir()
	{
		ArrayList<File> files = new ArrayList<>();
		files.add(new File(Config.DATAPACK_ROOT, "data/skill_tree/"));
		return files;
	}

	@Override
	public boolean isIgnored(File b)
	{
		return false;
	}

	@Override
	public String getDTDFileName()
	{
		return "tree.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator("certification_skill_tree"); iterator.hasNext(); )
		{
			getHolder().addAllCertificationLearns(parseSkillLearn(iterator.next()));
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("certification_double_skill_tree"); iterator.hasNext(); )
		{
			getHolder().addAllCertificationDoubleLearns(parseSkillLearn(iterator.next()));
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("sub_unit_skill_tree"); iterator.hasNext(); )
		{
			getHolder().addAllSubUnitLearns(parseSkillLearn(iterator.next()));
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("pledge_skill_tree"); iterator.hasNext(); )
		{
			getHolder().addAllPledgeLearns(parseSkillLearn(iterator.next()));
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("collection_skill_tree"); iterator.hasNext(); )
		{
			getHolder().addAllCollectionLearns(parseSkillLearn(iterator.next()));
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("fishing_skill_tree"); iterator.hasNext(); )
		{
			Element nxt = iterator.next();
			for(Iterator<Element> classIterator = nxt.elementIterator("race"); classIterator.hasNext(); )
			{
				Element classElement = classIterator.next();
				int race = Integer.parseInt(classElement.attributeValue("id"));
				List<SkillLearn> learns = parseSkillLearn(classElement);
				getHolder().addAllFishingLearns(race, learns);
			}
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("transfer_skill_tree"); iterator.hasNext(); )
		{
			Element nxt = iterator.next();
			for(Iterator<Element> classIterator = nxt.elementIterator("class"); classIterator.hasNext(); )
			{
				Element classElement = classIterator.next();
				int classId = Integer.parseInt(classElement.attributeValue("id"));
				List<SkillLearn> learns = parseSkillLearn(classElement);
				getHolder().addAllTransferLearns(classId, learns);
			}
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("race_skill_tree"); iterator.hasNext(); )
		{
			Element nxt = iterator.next();
			for(Iterator<Element> classIterator = nxt.elementIterator("race"); classIterator.hasNext(); )
			{
				Element classElement = classIterator.next();
				int race = Integer.parseInt(classElement.attributeValue("id"));
				List<SkillLearn> learns = parseSkillLearn(classElement);
				getHolder().addAllRaceLearns(race, learns);
			}
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("normal_skill_tree"); iterator.hasNext(); )
		{
			HashMap<Integer, List<SkillLearn>> map = new HashMap<Integer, List<SkillLearn>>();
			Element nxt = iterator.next();
			for(Iterator<Element> classIterator = nxt.elementIterator("class"); classIterator.hasNext(); )
			{
				Element classElement = classIterator.next();
				int classId = Integer.parseInt(classElement.attributeValue("id"));
				List<SkillLearn> learns = parseSkillLearn(classElement);

				map.put(classId, learns);
			}

			getHolder().addAllNormalSkillLearns(map);
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("transformation_skill_tree"); iterator.hasNext(); )
		{
			Element nxt = iterator.next();
			for(Iterator<Element> classIterator = nxt.elementIterator("race"); classIterator.hasNext(); )
			{
				Element classElement = classIterator.next();
				int race = Integer.parseInt(classElement.attributeValue("id"));
				List<SkillLearn> learns = parseSkillLearn(classElement);
				getHolder().addAllTransformationLearns(race, learns);
			}
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("awakening_keep_skill_tree"); iterator.hasNext(); )
		{
			//HashMap<Integer, HashMap<Integer, List<Integer>>> map = new HashMap<>();
			HashMap<Integer, HashMap<Integer, List<Integer>>> map = new HashMap<Integer, HashMap<Integer, List<Integer>>>();
			Element nxt = iterator.next();
			for(Iterator<Element> awakenClassIterator = nxt.elementIterator("awakenClass"); awakenClassIterator.hasNext(); )
			{
				Element awakenClass = awakenClassIterator.next();
				int awakenClassId = Integer.parseInt(awakenClass.attributeValue("id"));
				//HashMap<Integer, List<Integer>> transferClassList = new HashMap<>();
				HashMap<Integer, List<Integer>> transferClassList = new HashMap<Integer, List<Integer>>();
				for(Iterator<Element> fromClassIterator = awakenClass.elementIterator("fromClass"); fromClassIterator.hasNext(); )
				{
					Element fromClass = fromClassIterator.next();
					int fromClassId = Integer.parseInt(fromClass.attributeValue("id"));
					List<Integer> keepSkill = parseKeepSkill(fromClass);
					transferClassList.put(fromClassId, keepSkill);
				}
				map.put(awakenClassId, transferClassList);
			}
			getHolder().addSkillsToMaintain(map);
		}
	}

	private List<SkillLearn> parseSkillLearn(Element tree)
	{
		List<SkillLearn> skillLearns = new ArrayList<SkillLearn>();
		for(Iterator<Element> iterator = tree.elementIterator("skill"); iterator.hasNext(); )
		{
			Element element = iterator.next();

			int id = Integer.parseInt(element.attributeValue("id"));
			int level = Integer.parseInt(element.attributeValue("level"));
			int cost = element.attributeValue("cost") == null ? 0 : Integer.parseInt(element.attributeValue("cost"));
			int min_level = Integer.parseInt(element.attributeValue("min_level"));
			int item_id = element.attributeValue("item_id") == null ? 0 : Integer.parseInt(element.attributeValue("item_id"));
			long item_count = element.attributeValue("item_count") == null ? 1 : Long.parseLong(element.attributeValue("item_count"));
			boolean clicked = element.attributeValue("clicked") != null && Boolean.parseBoolean(element.attributeValue("clicked"));

			skillLearns.add(new SkillLearn(id, level, min_level, cost, item_id, item_count, clicked));
		}

		return skillLearns;
	}

	private List<Integer> parseKeepSkill(Element tree)
	{
		//List<Integer> skillRemove = new ArrayList<>();
		List<Integer> skillRemove = new ArrayList<Integer>();
		for(Iterator<Element> iterator = tree.elementIterator("keepSkill"); iterator.hasNext(); )
		{
			Element element = iterator.next();
			int id = Integer.parseInt(element.attributeValue("id"));
			skillRemove.add(id);
		}
		return skillRemove;
	}
}
