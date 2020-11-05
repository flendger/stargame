package ru.flendger.game.sprite;

import ru.flendger.game.base.BaseShip;
import ru.flendger.game.base.EnemySettingsDto;
import ru.flendger.game.math.Rect;
import ru.flendger.game.pool.BulletPool;
import ru.flendger.game.pool.ExplosionPool;

public class EnemyShip extends BaseShip {

    private static final float START_V_Y = -0.3f;

    public EnemyShip(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds) {
        super(bulletPool, explosionPool, worldBounds);
    }

    @Override
    public void update(float delta) {
        bulletPos.set(pos.x, getBottom());

        super.update(delta);
        if (getTop() <= worldBounds.getTop()) {
            v.set(v0);
        } else {
            curDeltaShoot = reloadDelta - delta * 2;
        }

        if (getTop() < worldBounds.getBottom()) {
            destroy();
        }
    }

    public void set(EnemySettingsDto settings) {
        this.regions = settings.getRegions();
        this.v0.set(settings.getV0());
        this.bulletRegion = settings.getBulletRegion();
        this.bulletHeight = settings.getBulletHeight();
        this.bulletV.set(settings.getBulletV());
        this.bulletSound = settings.getBulletSound();
        this.bulletDamage = settings.getDamage();
        this.reloadDelta = settings.getReloadInterval();
        setHeightProportion(settings.getHeight());
        this.hp = settings.getHp();
        this.curDeltaShoot = 0f;
        this.v.set(0, START_V_Y);
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                        || bullet.getLeft() > getRight()
                        || bullet.getBottom() > getTop()
                        || bullet.getTop() < pos.y
        );
    }
}
