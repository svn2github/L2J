package l2next.gameserver.listener.reflection;

import l2next.commons.listener.Listener;
import l2next.gameserver.model.entity.Reflection;

public interface OnReflectionCollapseListener extends Listener<Reflection>
{
	public void onReflectionCollapse(Reflection reflection);
}
