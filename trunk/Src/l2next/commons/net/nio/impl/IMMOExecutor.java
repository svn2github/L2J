package l2next.commons.net.nio.impl;

public interface IMMOExecutor<T extends MMOClient>
{
	public void execute(Runnable r);
}