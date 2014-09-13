package l2next.gameserver.templates.item;

public enum Grade
{
	NONE(ItemTemplate.CRYSTAL_NONE, 0),
	D(ItemTemplate.CRYSTAL_D, 1),
	C(ItemTemplate.CRYSTAL_C, 2),
	B(ItemTemplate.CRYSTAL_B, 3),
	A(ItemTemplate.CRYSTAL_A, 4),
	S(ItemTemplate.CRYSTAL_S, 5),
	S80(ItemTemplate.CRYSTAL_S, 5),
	R(ItemTemplate.CRYSTAL_R, 6),
	R95(ItemTemplate.CRYSTAL_R, 6),
	R99(ItemTemplate.CRYSTAL_R, 6);
	/**
	 * ID соответствующего грейду кристалла
	 */
	public final int cry;
	/**
	 * ID грейда, без учета уровня S
	 */
	public final int externalOrdinal;

	private Grade(int crystal, int ext)
	{
		cry = crystal;
		externalOrdinal = ext;
	}
}
