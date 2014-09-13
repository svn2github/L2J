package l2next.gameserver.utils.hwid;

public class HardwareID
{
	private static final HwidComparator DefaultComparator = new HwidComparator();

	public static final boolean COMPARE_CPU = true;
	public static final boolean COMPARE_BIOS = true;
	public static final boolean COMPARE_HDD = true;
	public static final boolean COMPARE_MAC = true;
	public static final boolean COMPARE_WLInternal = false;

	public final int CPU, WLInternal;
	public final long BIOS, HDD, MAC;
	public final String hwid;

	public HardwareID(String hwid)
	{
		this.hwid = hwid;
		CPU = Integer.decode("0x" + hwid.substring(0, 4));
		BIOS = Long.decode("0x" + hwid.substring(4, 12));
		HDD = Long.decode("0x" + hwid.substring(12, 20));
		MAC = Long.decode("0x" + hwid.substring(20, 28));
		WLInternal = Integer.decode("0x" + hwid.substring(28, 32));
	}

	@Override
	public int hashCode()
	{
		StringBuilder hwidString = new StringBuilder();
		if(COMPARE_CPU)
		{
			hwidString.append(hwid.substring(0, 4));
		}
		if(COMPARE_BIOS)
		{
			hwidString.append(hwid.substring(4, 12));
		}
		if(COMPARE_HDD)
		{
			hwidString.append(hwid.substring(12, 20));
		}
		if(COMPARE_MAC)
		{
			hwidString.append(hwid.substring(20, 28));
		}
		if(COMPARE_WLInternal)
		{
			hwidString.append(hwid.substring(28, 32));
		}
		return hwidString.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null || !(obj instanceof HardwareID))
		{
			return false;
		}

		return DefaultComparator.compare(this, (HardwareID) obj) == HwidComparator.EQUALS;
	}

	@Override
	public String toString()
	{
		return String.format("%s [CPU: %04x, BIOS: %08x, HDD: %08x, MAC: %08x, WLInternal: %04x]", hwid, CPU, BIOS, HDD, MAC, WLInternal);
	}
}