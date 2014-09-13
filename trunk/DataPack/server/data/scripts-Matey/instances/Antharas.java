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

public class Antharas extends Reflection
{
	
 private static int antharas = 29068;
    private Location antharasCoords = new Location(181911, 114835, -7678, 32542);

    @Override
    protected void onCreate() {
        super.onCreate();
        this.addSpawnWithoutRespawn(antharas, antharasCoords, 0);
    }
}