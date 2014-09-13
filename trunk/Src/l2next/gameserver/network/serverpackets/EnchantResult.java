package l2next.gameserver.network.serverpackets;

public class EnchantResult extends L2GameServerPacket
{
	private final int _resultId, _crystalId;
	private final long _count;
	private final int _enchantValue;

	public static final EnchantResult SUCESS = new EnchantResult(0, 0, 0); // вещь
	// заточилась
	// public static final EnchantResult FAILED = new EnchantResult(1, 0, 0); //
	// вещь разбилась, требует указания получившихся кристаллов, в статичном
	// виде не используется
	public static final EnchantResult CANCEL = new EnchantResult(2, 0, 0); // заточка
	// невозможна
	public static final EnchantResult BLESSED_FAILED = new EnchantResult(3, 0, 0); // заточка
	// не
	// удалась,
	// уровень
	// заточки
	// сброшен
	// на
	// 0
	public static final EnchantResult FAILED_NO_CRYSTALS = new EnchantResult(4, 0, 0); // вещь
	// разбилась,
	// но
	// кристаллов
	// не
	// получилось
	// (видимо
	// для
	// эвента,
	// сейчас
	// использовать
	// невозможно,
	// там
	// заглушка)
	public static final EnchantResult ANCIENT_FAILED = new EnchantResult(5, 0, 0); // заточка
	// не
	// удалась,
	// уровень
	// заточки
	// не
	// изменен
	// (для
	// Ancient
	// Enchant
	// Crystal
	// из
	// итем
	// молла)

	public EnchantResult(int resultId, int crystalId, long count)
	{
		this(resultId, crystalId, count, 0);
	}

	public EnchantResult(int resultId, int crystalId, long count, int enchantValue)
	{
		_resultId = resultId;
		_crystalId = crystalId;
		_count = count;
		_enchantValue = enchantValue;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_resultId);
		writeD(_crystalId); // item id кристаллов
		writeQ(_count); // количество кристаллов
		writeD(_enchantValue);// God 415
	}
}