package l2next.gameserver.utils.timetrac;

import org.apache.log4j.Logger;

public class Tick
{
	protected final static Logger LOGGER = Logger.getLogger(Tick.class);

	public long startTime = 0l;
	public long stopTime = 0l;
	public long startFreeMemory = 0l;
	public long stopFreeMemory = 0l;

	public Tick()
	{
	}

	public Tick(long startTime, long stopTime)
	{
		this.startTime = startTime;
		this.stopTime = stopTime;
		this.startFreeMemory = Runtime.getRuntime().freeMemory();
	}

	public Tick(long startTime)
	{
		this.startTime = startTime;
		this.startFreeMemory = Runtime.getRuntime().freeMemory();
	}

	public void stop()
	{
		this.stopTime = System.currentTimeMillis();
		this.stopFreeMemory = Runtime.getRuntime().freeMemory();
	}

	public long duration()
	{
		return stopTime - startTime;
	}

	public void log()
	{
		// TODO[AlkBy]: memory display doesn't work yet
		LOGGER.info("tick duration [" + duration() + "]" + TimeTracker.MLS + ", [" + (stopFreeMemory - startFreeMemory) + "]" + TimeTracker.BYTES + " used, free memory remains [" + stopFreeMemory + "]" + TimeTracker.BYTES);
	}
}
