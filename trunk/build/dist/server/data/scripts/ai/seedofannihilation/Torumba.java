package ai.seedofannihilation;

import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.instances.NpcInstance;

// Если его ударить включается таймер 20 минут, если не убить его за 20 минут, он исчезает и появляеться на месте респавна через 5 минут с полным ХП. (за 2 мин до исчезновения появляеться надпись, что РБ готовится к исчезновению)
// Двигаеться крайне редко, даже если ударить и отойти от него, он будет нон-стоп кастовать 6404. В ближнем бою бьет как файтер. Крайне редко, примерно 2 раза за 20 минут, дает травилку 6402, если получить травилку еще раз левел увеличивается (Если на персонаже висит травилка, торумба постоянно пользуеться этим скиллом увеличивая левел травилки вплоть до 5 лвл).
// Ставит в паралич всех (6406), кроме трансформы Бестикатонов.
// У трансформы есть скилл Коготь Такракхана. Если им бить РБ, и наносить 10 ударов за 20 секунд, то после каждых 10 ударов появляеться надпись что торумба ранен. Всего 9 надписей, после 9 надписей и 10 ударов он умирает. Если не успеть нанести 10 ударов за 20 сеунд, кол-во появившихся надписей обнуляеться.
public class Torumba extends Fighter
{

	public Torumba(NpcInstance actor)
	{
		super(actor);
	}
}