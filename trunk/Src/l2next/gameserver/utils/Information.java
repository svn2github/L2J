package l2next.gameserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Information
{
	private static final Logger _log = LoggerFactory.getLogger(Information.class);
	private static Information _instance;

	public static Information getInstance()
	{
		if(_instance == null)
		{
			_instance = new Information();
		}
		return _instance;
	}

	public Information()
	{
		_log.info("==================#INFORMATION#==================");
		_log.info("Chronicle:		Lindvior");
		_log.info("Version:		PreRealised" );
		_log.info("Author:		HellsingProject");
		_log.info("=================================================");
	}
}