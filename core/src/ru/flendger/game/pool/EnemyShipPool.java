package ru.flendger.game.pool;


import ru.flendger.game.base.SpritesPool;
import ru.flendger.game.math.Rect;
import ru.flendger.game.sprite.EnemyShip;

public class EnemyShipPool extends SpritesPool<EnemyShip> {

    private final BulletPool bulletPool;
    private final Rect worldBounds;

    public EnemyShipPool(BulletPool bulletPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(bulletPool, worldBounds);
    }
}
