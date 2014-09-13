package l2next.gameserver.data.xml.parser;

import l2next.commons.data.xml.DocumentParser;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.NpcHolder;
import l2next.gameserver.model.reward.RewardData;
import l2next.gameserver.model.reward.RewardGroup;
import l2next.gameserver.model.reward.RewardList;
import l2next.gameserver.model.reward.RewardType;
import l2next.gameserver.templates.npc.NpcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class DropListDataParser extends DocumentParser
{
	private static final Logger _log = LoggerFactory.getLogger(DropListDataParser.class);
	private static DropListDataParser _instance = new DropListDataParser();

	private int _dropsParsed;
	private int _spoilsParsed;

	public static final DropListDataParser getInstance()
	{
		if(_instance == null)
		{
			_instance = new DropListDataParser();
		}
		return _instance;
	}

	public void load()
	{
		_dropsParsed = (_spoilsParsed = 0);
		parseDirectory(Config.DATAPACK_ROOT + "/data/stats/npc/droplist/");
		_log.info("Loaded " + _dropsParsed + " drops & " + _spoilsParsed + " spoils.");
	}

	public void reload()
	{
		load();
	}

	protected void parseDocument()
	{
		for(Node globalNode = getCurrentDocument().getFirstChild(); globalNode != null; globalNode = globalNode.getNextSibling())
		{
			if(!"list".equalsIgnoreCase(globalNode.getNodeName()))
			{
				continue;
			}
			for(Node npcNode = globalNode.getFirstChild(); npcNode != null; npcNode = npcNode.getNextSibling())
			{
				if(!"npc".equalsIgnoreCase(npcNode.getNodeName()))
				{
					continue;
				}
				NamedNodeMap attrs = npcNode.getAttributes();
				RewardList list = null;
				RewardType type = null;
				int npcId = parseInt(attrs, "id");
				NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
				if(template == null)
				{
					_log.warn("Omitted NPC ID: " + npcId + " - NPC template does not exists!");
				}
				else
				{
					for(Node dropNode = npcNode.getFirstChild(); dropNode != null; dropNode = dropNode.getNextSibling())
					{
						if("droplist".equalsIgnoreCase(dropNode.getNodeName()))
						{
							type = RewardType.RATED_GROUPED;
							list = new RewardList(type, false);

							for(Node catNode = dropNode.getFirstChild(); catNode != null; catNode = catNode.getNextSibling())
							{
								if(!"category".equalsIgnoreCase(catNode.getNodeName()))
								{
									continue;
								}
								attrs = catNode.getAttributes();
								int chance = (int) (parseDouble(attrs, "chance") * 10000.0D);
								RewardGroup group = new RewardGroup(chance);
								for(Node itemNode = catNode.getFirstChild(); itemNode != null; itemNode = itemNode.getNextSibling())
								{
									if(!"item".equalsIgnoreCase(itemNode.getNodeName()))
									{
										continue;
									}
									_dropsParsed += 1;

									attrs = itemNode.getAttributes();
									RewardData data = parseReward(attrs);
									group.addData(data);
								}
								list.add(group);
							}
						}
						else
						{
							if(!"spoillist".equalsIgnoreCase(dropNode.getNodeName()))
							{
								continue;
							}
							RewardGroup g = new RewardGroup(RewardList.MAX_CHANCE);
							type = RewardType.SWEEP;
							list = new RewardList(type, false);
							for(Node itemNode = dropNode.getFirstChild(); itemNode != null; itemNode = itemNode.getNextSibling())
							{
								if(!"item".equalsIgnoreCase(itemNode.getNodeName()))
								{
									continue;
								}
								_spoilsParsed += 1;

								attrs = itemNode.getAttributes();
								RewardData data = parseReward(attrs);
								g.addData(data);
							}
							list.add(g);
						}
						template.putRewardList(type, list);
					}
				}
			}
		}
	}

	private RewardData parseReward(NamedNodeMap attrs)
	{
		int itemId = parseInt(attrs, "id");
		int min = parseInt(attrs, "min");
		int max = parseInt(attrs, "max");
		// переводим в системный вид
		double chance = parseDouble(attrs, "chance") * 10000.0D;

		RewardData data = new RewardData(itemId);
		if(data.getItem().isCommonItem())
		{
			data.setChance(chance * Config.RATE_DROP_COMMON_ITEMS);
		}
		else
		{
			data.setChance(chance);
		}

		data.setMinDrop(min);
		data.setMaxDrop(max);

		return data;
	}
}