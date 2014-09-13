package l2next.gameserver.utils.timetrac;

import java.util.HashMap;
import java.util.Map;

/**
 * For testing and measuring the execution time (optimization)
 *
 * @author AlkBy
 */
public class TimeTracker
{

	public static final String MLS = "mls";
	public static final String BYTES = "bytes";

	/**
	 * Name of global operation and 2 values - start and stop times
	 */
	private Map<String, Operation> operations = new HashMap<String, Operation>();

	/**
	 * Старт замера времени по названию операции(Например, метода)
	 *
	 * @param operation
	 */
	public void start(final String operation)
	{
		operations.put(operation, new Operation(operation));
	}

	/**
	 * Таймер позволяющий в любое время замерять продолжительность определенного действия
	 *
	 * @param tickName
	 */
	public void tickStart(final String operation, final String tickName)
	{
		if(!operations.containsKey(operation))
		{
			throw new IllegalStateException("Can not find start of [" + operation + "] operation");
		}
		operations.get(operation).startTick(tickName);
	}

	public void tickStop(final String operation, final String tickName)
	{
		if(!operations.containsKey(operation))
		{
			throw new IllegalStateException("Can not find start of [" + operation + "] operation");
		}
		operations.get(operation).stopTick(tickName);
	}

	/**
	 * Остановить таймер с названием любой операции
	 *
	 * @param operation
	 */
	public void stop(final String operation)
	{
		operations.get(operation).stop();
	}

	/**
	 * Вывод полного времени выполнения (Например, метода)
	 *
	 * @param operation
	 */
	public void printOperationTime(final String operation)
	{
		assertOperation(operation);
		operations.get(operation).logOperation();
	}

	public void printOperationsTime()
	{
		for(Operation operation : operations.values())
		{
			operation.logOperation();
		}
	}

	/**
	 * Вывод времени, каждого пика(например, таймера во время выполнения цикла)
	 */
	public void printTickTimes(final String operation, final String tickName)
	{
		assertOperation(operation);
		operations.get(operation).logTick(tickName);
	}

	public void printTicksTimes(final String operation)
	{
		assertOperation(operation);
		operations.get(operation).logTicks();
	}

	/**
	 * TODO: print amount of memory all ticks used.
	 *
	 * @param operation
	 * @param tickName
	 */
	public void printTicksDuration(final String operation, final String tickName)
	{
		assertOperation(operation);
		operations.get(operation).logTickDuration(tickName);
	}

	public void printTicksDuration(final String operation)
	{
		assertOperation(operation);
		operations.get(operation).logTickDuration();
	}

	public void printTickAverage(final String operation, final String tickName)
	{
		assertOperation(operation);
		operations.get(operation).logTickAverage(tickName);
	}

	public void printTicksAverage(final String operation)
	{
		assertOperation(operation);
		operations.get(operation).logTicksAverage();
	}

	public void printWholeOperationInfo(final String operation)
	{
		printOperationTime(operation);
		printTicksDuration(operation);
		printTicksAverage(operation);
	}

	public void printWholeOperationsInfo()
	{
		for(String operation : operations.keySet())
		{
			printOperationTime(operation);
			printTicksTimes(operation);
			printTicksDuration(operation);
			printTicksAverage(operation);
		}
	}

	private void assertOperation(String operation)
	{
		if(!operations.containsKey(operation))
		{
			throw new IllegalStateException("Can not find [" + operation + "] operation");
		}
	}
}
