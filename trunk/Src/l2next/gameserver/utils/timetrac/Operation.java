package l2next.gameserver.utils.timetrac;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Currently we do not support ticks inside other ticks. Also ticking is not thread saved.
 */
public class Operation extends Tick
{
	protected final static Logger LOGGER = Logger.getLogger(Operation.class);
	public String name;
	public Map<String, List<Tick>> ticks = new HashMap<String, List<Tick>>();
	public String tickNameCandidate;
	public Tick tickCandidate;

	public Operation()
	{
	}

	public Operation(String name)
	{
		super(System.currentTimeMillis());
		this.name = name;
	}

	public Operation(String name, long startTime)
	{
		super(startTime);
		this.name = name;
	}

	public Operation(String name, long startTime, long stopTime)
	{
		super(startTime, stopTime);
		this.name = name;
	}

	public void startTick(String tickName)
	{
		if(null != tickCandidate)
		{
			throw new IllegalStateException("Tick for the name [" + tickName + "] is already started");
		}
		tickCandidate = new Tick(System.currentTimeMillis());
		tickNameCandidate = tickName;
	}

	public void stopTick(String tickName)
	{
		if(!tickNameCandidate.equals(tickName))
		{
			throw new IllegalStateException("Tick started for the tick name[" + tickNameCandidate + "], but now we " +
				"try to finish it for [" + tickName + "]");
		}
		if(null == tickCandidate)
		{
			throw new IllegalStateException("Tick for the name [" + tickName + "] was not started yet");
		}
		tickCandidate.stop();
		if(!ticks.containsKey(tickName))
		{
			// TODO:do it sortable
			ticks.put(tickName, new ArrayList<Tick>());
		}
		ticks.get(tickName).add(tickCandidate);
		tickCandidate = null;
		tickNameCandidate = null;
	}

	public void stop()
	{
		if(null != tickCandidate)
		{
			throw new IllegalStateException("Tick with tick name [" + tickNameCandidate + "]for this operation [" + name + "] not finished yet");
		}
		super.stop();
	}

	public void logOperation()
	{
		assertOperationStopped();
		LOGGER.info("total time of operation [" + name + "]:[" + (stopTime - startTime) + "]" + TimeTracker.MLS +
			", [" + (stopFreeMemory - startFreeMemory) + "]" + TimeTracker.BYTES + " used");
	}

	public void logTick(String tickName)
	{
		assertOperationStopped();
		if(!ticks.containsKey(tickName))
		{
			throw new IllegalStateException("Ticks for tick name [" + tickName + "] not found");
		}
		LOGGER.info("operation [" + name + "] ticks with names [" + tickName + "] info:");
		for(Tick tick : ticks.get(tickName))
		{
			tick.log();
		}
	}

	public void logTicks()
	{
		for(String tickName : ticks.keySet())
		{
			logTick(tickName);
		}
	}

	public long tickDuration(String tickName)
	{
		assertOperationStopped();
		if(!ticks.containsKey(tickName))
		{
			throw new IllegalStateException("Operation [" + name + "] ticks with name [" + tickName + "] not found");
		}
		long result = 0l;
		for(Tick tick : ticks.get(tickName))
		{
			result += tick.duration();
		}
		return result;
	}

	public void logTickDuration(String tickName)
	{
		LOGGER.info("operation [" + name + "] ticks with names [" + tickName + "] duration is [" + tickDuration(tickName) + "]" + TimeTracker.MLS);
	}

	public void logTickDuration()
	{
		assertOperationStopped();
		for(String tickName : ticks.keySet())
		{
			logTickDuration(tickName);
		}
	}

	public void logTickAverage(String tickName)
	{
		assertOperationStopped();
		long duration = duration();
		if(0 == duration)
		{
			LOGGER.info("Ticks with names [" + tickName + "] took 0 mls, can not calculate percentage of operation [" + name + "]");
		}
		else
		{
			LOGGER.info("Ticks with names [" + tickName + "] took [" + ((double) tickDuration(tickName)) / duration * 100 + "]% of operation [" + name + "]");
		}
	}

	public void logTicksAverage()
	{
		assertOperationStopped();
		for(String tickName : ticks.keySet())
		{
			logTickAverage(tickName);
		}
	}

	private void assertOperationStopped()
	{
		if(stopTime == 0l)
		{
			throw new IllegalStateException("Operation [" + name + "] was not finished");
		}
	}
}
