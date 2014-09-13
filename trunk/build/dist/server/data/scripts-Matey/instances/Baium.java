package instances;


import l2next.gameserver.model.entity.Reflection;

import l2next.gameserver.utils.Location;

/**
 * Класс контролирует Валлока, рб 99 лвл
 *
 * @author Awakeninger
 * Логгеры поставлены исключительно на время тестов.
 * Чтобы видеть стадии инстанса.
 *
 * По факту - это просто болванка инстанса.
 */

public class Baium extends Reflection
{
	
 private static int Baium = 20432;
    private Location BaiumCoords = new Location(115996, 17417, 10106, 41740);  

    @Override
    protected void onCreate() {
        super.onCreate();
        this.addSpawnWithoutRespawn(Baium, BaiumCoords, 0);
    }
}