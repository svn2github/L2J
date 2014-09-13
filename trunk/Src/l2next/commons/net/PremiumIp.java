package l2next.commons.net;

/**
 * User: Samurai
 */
public class PremiumIp extends Ip implements Comparable
{
	private long time;

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	@Override
	public int compareTo(Object o)
	{
		PremiumIp premiumIp = (PremiumIp) o;
		if(this.time < premiumIp.time)
		{
		    /* текущее меньше полученного */
			return -1;
		}
		else if(this.time > premiumIp.time)
		{
	        /* текущее больше полученного */
			return 1;
		}
        /* текущее равно полученному */
		return 0;
	}
}