package l2next.gameserver.geodata;

import l2next.commons.geometry.Polygon;
import l2next.gameserver.model.entity.Reflection;

import java.util.HashMap;

public interface GeoControl
{
	public abstract Polygon getGeoPos();

	public abstract HashMap<Long, Byte> getGeoAround();

	public abstract void setGeoAround(HashMap<Long, Byte> value);

	public abstract Reflection getReflection();

	public abstract boolean isGeoCloser();
}