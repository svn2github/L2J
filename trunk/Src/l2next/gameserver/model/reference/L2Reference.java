package l2next.gameserver.model.reference;

import l2next.commons.lang.reference.AbstractHardReference;

public class L2Reference<T> extends AbstractHardReference<T>
{
	public L2Reference(T reference)
	{
		super(reference);
	}
}
