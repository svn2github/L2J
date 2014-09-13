package l2next.gameserver.utils.hwid;

import l2next.commons.collections.GCSArray;

import java.util.Comparator;
import java.util.List;

public class HwidComparator implements Comparator<HardwareID>
{
	public static final int EQUALS = 0;
	public static final int NOT_EQUALS = 1;

	public boolean COMPARE_CPU = HardwareID.COMPARE_CPU;
	public boolean COMPARE_BIOS = HardwareID.COMPARE_BIOS;
	public boolean COMPARE_HDD = HardwareID.COMPARE_HDD;
	public boolean COMPARE_MAC = HardwareID.COMPARE_MAC;
	public boolean COMPARE_WLInternal = HardwareID.COMPARE_WLInternal;

	public HwidComparator()
	{
	}

	public HwidComparator(boolean mac)
	{
		COMPARE_MAC = mac;
	}

	public HwidComparator(boolean mac, boolean cpu)
	{
		COMPARE_MAC = mac;
		COMPARE_CPU = cpu;
	}

	public HwidComparator(boolean mac, boolean cpu, boolean bios)
	{
		COMPARE_MAC = mac;
		COMPARE_CPU = cpu;
		COMPARE_BIOS = bios;
	}

	public HwidComparator(boolean mac, boolean cpu, boolean bios, boolean hdd)
	{
		COMPARE_MAC = mac;
		COMPARE_CPU = cpu;
		COMPARE_HDD = hdd;
		COMPARE_BIOS = bios;
	}

	public HwidComparator(boolean mac, boolean cpu, boolean bios, boolean hdd, boolean _WLInternal)
	{
		COMPARE_MAC = mac;
		COMPARE_CPU = cpu;
		COMPARE_HDD = hdd;
		COMPARE_BIOS = bios;
		COMPARE_WLInternal = _WLInternal;
	}

	@Override
	public int compare(HardwareID o1, HardwareID o2)
	{
		if(o1 == null || o2 == null)
		{
			return o1 == o2 ? EQUALS : NOT_EQUALS;
		}
		if(COMPARE_CPU && o1.CPU != o2.CPU)
		{
			return NOT_EQUALS;
		}
		if(COMPARE_BIOS && o1.BIOS != o2.BIOS)
		{
			return NOT_EQUALS;
		}
		if(COMPARE_HDD && o1.HDD != o2.HDD)
		{
			return NOT_EQUALS;
		}
		if(COMPARE_MAC && o1.MAC != o2.MAC)
		{
			return NOT_EQUALS;
		}
		if(COMPARE_WLInternal && o1.WLInternal != o2.WLInternal)
		{
			return NOT_EQUALS;
		}
		return EQUALS;
	}

	public int find(HardwareID hwid, List<HardwareID> in)
	{
		for(int i = 0; i < in.size(); i++)
		{
			if(compare(hwid, in.get(i)) == EQUALS)
			{
				return i;
			}
		}
		return -1;
	}

	public boolean contains(HardwareID hwid, List<HardwareID> in)
	{
		return find(hwid, in) != -1;
	}

	public boolean contains(String hwid, List<HardwareID> in)
	{
		return find(new HardwareID(hwid), in) != -1;
	}

	public boolean remove(HardwareID hwid, List<HardwareID> in)
	{
		int i = find(hwid, in);
		return i != -1 && in.remove(i) != null;
	}

	public int find(HardwareID hwid, GCSArray<HardwareID> in)
	{
		for(int i = 0; i < in.size(); i++)
		{
			if(compare(hwid, in.get(i)) == EQUALS)
			{
				return i;
			}
		}
		return -1;
	}

	public boolean contains(HardwareID hwid, GCSArray<HardwareID> in)
	{
		return find(hwid, in) != -1;
	}

	public boolean remove(HardwareID hwid, GCSArray<HardwareID> in)
	{
		int i = find(hwid, in);
		return i != -1 && in.remove(i) != null;
	}
}
