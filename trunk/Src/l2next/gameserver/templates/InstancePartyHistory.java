package l2next.gameserver.templates;

import l2next.gameserver.data.xml.holder.ClassDataHolder;
import java.util.List;

public class InstancePartyHistory
{
	private List<ClassDataHolder> _charsInParty;
	private int _instanceId;
	private int _instanceUseTime;
	private int _instanceStatus;

	public List<ClassDataHolder> getCharsInParty()
	{
		return _charsInParty;
	}

	public void setCharsInParty(List<ClassDataHolder> charsInParty)
	{
		_charsInParty = charsInParty;
	}

	public int getInstanceId()
	{
		return _instanceId;
	}

	public void setInstanceId(int instanceId)
	{
		_instanceId = instanceId;
	}

	public int getInstanceUseTime()
	{
		return _instanceUseTime;
	}

	public void setInstanceUseTime(int instanceUseTime)
	{
		_instanceUseTime = instanceUseTime;
	}

	public int getInstanceStatus()
	{
		return _instanceStatus;
	}

	public void setInstanceStatus(int instanceStatus)
	{
		_instanceStatus = instanceStatus;
	}
}