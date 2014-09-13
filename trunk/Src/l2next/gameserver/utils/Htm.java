package l2next.gameserver.utils;

/**
 * User: Samurai
 */
public class Htm
{
	public static String button(String value, String bypass, int width, int height)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<button value=\"");
		sb.append(value);
		sb.append("\" action=\"bypass ");
		sb.append(bypass);
		sb.append("\" width=");
		sb.append(width);
		sb.append(" height=");
		sb.append(height);
		sb.append(" back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
		return sb.toString();
	}

	public static String buttonTD(String value, String bypass, int width, int height)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<td>");
		sb.append(button(value, bypass, width, height));
		sb.append("</td>");
		return sb.toString();
	}

	public static String buttonTDTR(String value, String bypass, int width, int height)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>");
		sb.append(buttonTD(value, bypass, width, height));
		sb.append("</tr>");
		return sb.toString();
	}
}