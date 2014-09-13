package l2next.gameserver.model.abstracts;

import java.util.List;

public class InstanceHistory
{
	private List<CharacterClass> _charsInParty;
	private int _instanceId;
	private int _instanceUseTime;
	private int _instanceStatus;

	public List<CharacterClass> getCharsInParty()
	{
		return _charsInParty;
	}

	public void setCharsInParty(List<CharacterClass> charsInParty)
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