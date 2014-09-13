package l2next.gameserver.loginservercon.lspackets;

import l2next.gameserver.loginservercon.ReceivablePacket;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;

public class IsLocIP extends ReceivablePacket
{
	private int _objId;
	private String _ip;

	@Override
	protected void readImpl()
	{
		_objId = readD();
		_ip = readS();
	}

	@Override
	protected void runImpl()
	{
		Player player = GameObjectsStorage.getPlayer(_objId);

		if(player == null)
		{
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>");
		sb.append("<center>Привязка IP</center><br><br>");
		sb.append("Ваш текущий IP: <font color=00ff00>" + player.getIP() + "</font><br>");
		if(_ip.isEmpty())
		{
			sb.append("<font color=ff0000>Привязка IP отключена! </font> <br>");
			sb.append("Что бы привязать свой аккаунт к IP введите команду .loc on <br>");
		}
		else
		{
			sb.append("<font color=00ff00>Привязка IP включена! </font> <br>");
			sb.append("Что бы отвязать свой аккаунт от IP введите команду .loc off <br>");
		}
		sb.append("<font color=ff0000>НЕ включайте привязку IP, в случаи, если Ваш IP динамический! </font> <br>");
		sb.append("</body></html>");

		NpcHtmlMessage msg = new NpcHtmlMessage(player, null);
		msg.setHtml(sb.toString());
		player.sendPacket(msg);
	}

}
