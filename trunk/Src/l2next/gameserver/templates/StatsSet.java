package l2next.gameserver.templates;

import l2next.commons.collections.MultiValueSet;

public class StatsSet extends MultiValueSet<String>
{
	private static final long serialVersionUID = -2209589233655930756L;

	public static final StatsSet EMPTY = new StatsSet()
	{
		private static final long serialVersionUID = -1323089573817726580L;

		@Override
		public Object put(String a, Object a2)
		{
			throw new UnsupportedOperationException();
		}
	};

	public StatsSet()
	{
		super();
	}

	public StatsSet(StatsSet set)
	{
		super(set);
	}

	@Override
	public StatsSet clone()
	{
		return new StatsSet(this);
	}
}