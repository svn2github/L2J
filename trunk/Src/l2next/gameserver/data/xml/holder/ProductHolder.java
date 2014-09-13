package l2next.gameserver.data.xml.holder;

import l2next.gameserver.Config;
import l2next.gameserver.model.items.primeshop.PrimeItem;
import l2next.gameserver.model.items.primeshop.PrimeProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class ProductHolder
{
	private static Logger _log = LoggerFactory.getLogger(ProductHolder.class.getName());

	private static ProductHolder _instance = new ProductHolder();

	public static ProductHolder getInstance()
	{
		if(_instance == null)
		{
			_instance = new ProductHolder();
		}
		return _instance;
	}

	public void reload()
	{
		_instance = new ProductHolder();
	}

	public static int MAX_ITEMS_IN_RECENT_LIST;
	public static int PAYMENT_ITEM_ID;

	TreeMap<Integer, PrimeProduct> _itemsList;

	private ProductHolder()
	{
		_itemsList = new TreeMap<Integer, PrimeProduct>();

		try
		{
			File file = new File(Config.DATAPACK_ROOT, "data/item-mall.xml");
			DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
			factory1.setValidating(false);
			factory1.setIgnoringComments(true);
			Document doc1 = factory1.newDocumentBuilder().parse(file);

			for(Node n1 = doc1.getFirstChild(); n1 != null; n1 = n1.getNextSibling())
			{
				if("list".equalsIgnoreCase(n1.getNodeName()))
				{
					for(Node d1 = n1.getFirstChild(); d1 != null; d1 = d1.getNextSibling())
					{
						if("config".equalsIgnoreCase(d1.getNodeName()))
						{
							PAYMENT_ITEM_ID = d1.getAttributes().getNamedItem("payment_item_id") == null ? -1 : Integer.parseInt(d1.getAttributes().getNamedItem("payment_item_id").getNodeValue());
							MAX_ITEMS_IN_RECENT_LIST = d1.getAttributes().getNamedItem("recent_list_size") == null ? 5 : Integer.parseInt(d1.getAttributes().getNamedItem("recent_list_size").getNodeValue());
						}
						else if("product".equalsIgnoreCase(d1.getNodeName()))
						{
							Node onSaleNode = d1.getAttributes().getNamedItem("on_sale");
							Boolean onSale = onSaleNode != null && Boolean.parseBoolean(onSaleNode.getNodeValue());
							if(!onSale)
							{
								continue;
							}

							int productId = Integer.parseInt(d1.getAttributes().getNamedItem("id").getNodeValue());

							Node categoryNode = d1.getAttributes().getNamedItem("category");
							int category = categoryNode != null ? Integer.parseInt(categoryNode.getNodeValue()) : 5;

							Node priceNode = d1.getAttributes().getNamedItem("price");
							int price = priceNode != null ? Integer.parseInt(priceNode.getNodeValue()) : 0;

							Node isEventNode = d1.getAttributes().getNamedItem("is_event");
							Boolean isEvent = isEventNode != null && Boolean.parseBoolean(isEventNode.getNodeValue());

							Node isBestNode = d1.getAttributes().getNamedItem("is_best");
							Boolean isBest = isBestNode != null && Boolean.parseBoolean(isBestNode.getNodeValue());

							Node isNewNode = d1.getAttributes().getNamedItem("is_new");
							Boolean isNew = isNewNode != null && Boolean.parseBoolean(isNewNode.getNodeValue());

							Node tabIdNode = d1.getAttributes().getNamedItem("tabId");
							int tabId = Integer.parseInt(tabIdNode.getNodeValue());

							Node startTimeNode = d1.getAttributes().getNamedItem("sale_start_date");
							long startTimeSale = startTimeNode != null ? getMillisecondsFromString(startTimeNode.getNodeValue()) : 0;

							Node endTimeNode = d1.getAttributes().getNamedItem("sale_end_date");
							long endTimeSale = endTimeNode != null ? getMillisecondsFromString(endTimeNode.getNodeValue()) : 0;

							ArrayList<PrimeItem> components = new ArrayList<>();
							PrimeProduct pr = new PrimeProduct(productId, category, price, tabId, startTimeSale, endTimeSale);
							for(Node t1 = d1.getFirstChild(); t1 != null; t1 = t1.getNextSibling())
							{
								if("component".equalsIgnoreCase(t1.getNodeName()))
								{
									int item_id = Integer.parseInt(t1.getAttributes().getNamedItem("item_id").getNodeValue());
									int count = Integer.parseInt(t1.getAttributes().getNamedItem("count").getNodeValue());
									PrimeItem component = new PrimeItem(item_id, count);
									components.add(component);
								}
							}

							pr.setComponents(components);
							_itemsList.put(productId, pr);
						}
					}
				}
			}

			_log.info(String.format("ProductItemTable: Loaded %d product item on sale.", _itemsList.size()));
		}
		catch(Exception e)
		{
			_log.warn("ProductItemTable: Lists could not be initialized.");
			e.printStackTrace();
		}
	}

	private static int getProductTabId(boolean isEvent, boolean isBest, boolean isNew)
	{
		//TODO: Заюзать isNew
		if(isEvent && isBest)
		{
			return 3;
		}

		if(isEvent)
		{
			return 1;
		}

		if(isBest)
		{
			return 2;
		}

		return 4;
	}

	private static long getMillisecondsFromString(String datetime)
	{
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		try
		{
			Date time = df.parse(datetime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);

			return calendar.getTimeInMillis();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	public Collection<PrimeProduct> getAllItems()
	{
		return _itemsList.values();
	}

	public List<PrimeProduct> getItemsOnSale()
	{
		long currentTime = System.currentTimeMillis() / 1000L;
		List<PrimeProduct> _product = new ArrayList<PrimeProduct>();

		for(PrimeProduct item : getAllItems())
		{
			if(currentTime < item.getStartTimeSale() || currentTime > item.getEndTimeSale())
			{
				continue;
			}

			_product.add(item);
		}

		return _product;
	}

	public PrimeProduct getProduct(int id)
	{
		return _itemsList.get(id);
	}
}
