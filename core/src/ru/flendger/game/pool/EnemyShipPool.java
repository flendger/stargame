package ru.flendger.game.pool;


import ru.flendger.game.base.SpritesPool;
import ru.flendger.game.math.Rect;
import ru.flendger.game.sprite.EnemyShip;

public class EnemyShipPool extends SpritesPool<EnemyShip> {

    private final BulletPool bulletPool;
    private final Rect worldBounds;
    private final ExplosionPool explosionPool;

    public EnemyShipPool(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.explosionPool = explosionPool;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(bulletPool, explosionPool, worldBounds);
    }
}
