package l2next.gameserver.taskmanager;

import l2next.commons.threading.RunnableImpl;
import l2next.commons.threading.SteppingRunnableQueueManager;
import l2next.gameserver.Config;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.model.items.ItemInstance;

import java.util.concurrent.Future;

/**
 * @author ALF
 * @date 22.08.2012 Шаг выполенния задач 1000 мс.
 */
public class ItemsAutoDestroy extends SteppingRunnableQueueManager
{
	private static final ItemsAutoDestroy _instance = new ItemsAutoDestroy();

	public static final ItemsAutoDestroy getInstance()
	{
		return _instance;
	}

	private ItemsAutoDestroy()
	{
		super(1000L);

		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 1000L, 1000L);

		// Очистка каждую минуту
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl() throws Exception
			{
				ItemsAutoDestroy.this.purge();
			}

		}, 60000L, 60000L);
	}

	public Future<?> addHerb(final ItemInstance item)
	{
		item.setDropTime(System.currentTimeMillis());

		return schedule(new RunnableImpl()
		{

			@Override
			public void runImpl() throws Exception
			{
				item.deleteMe();
			}

		}, 60000);
	}

	public Future<?> addItem(final ItemInstance item)
	{
		item.setDropTime(System.currentTimeMillis());

		return schedule(new RunnableImpl()
		{

			@Override
			public void runImpl() throws Exception
			{
				item.deleteMe();
			}

		}, Config.AUTODESTROY_ITEM_AFTER);
	}
}