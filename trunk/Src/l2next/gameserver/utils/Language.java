package l2next.gameserver.utils;

/**
 * @author Awakeninger
 * @date 26.08.2013
 */
public enum Language
{
	MULTI("");

	public static final Language[] VALUES = Language.values();

	private String _shortName;

	Language(String shortName)
	{
		_shortName = shortName;
	}

	public String getShortName()
	{
		return _shortName;
	}
}
