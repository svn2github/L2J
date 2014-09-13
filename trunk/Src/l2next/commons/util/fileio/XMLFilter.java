package l2next.commons.util.fileio;

import java.io.File;
import java.io.FileFilter;

public class XMLFilter implements FileFilter
{
	public boolean accept(File pathname)
	{
		return pathname.getName().endsWith(".xml");
	}
}