package ru.flendger.game.sprite;

import ru.flendger.game.base.BaseShip;
import ru.flendger.game.base.EnemySettingsDto;
import ru.flendger.game.math.Rect;
import ru.flendger.game.pool.BulletPool;

public class EnemyShip extends BaseShip {
    private boolean beginShoot = false;
    private float acceleration;

    public EnemyShip(BulletPool bulletPool, Rect worldBounds) {
        super(bulletPool, worldBounds);
    }

    @Override
    public void update(float delta) {
        bulletPos.set(pos.x, getBottom());

        curDeltaShoot += delta;
        if (getTop() <= worldBounds.getTop()) {
            super.update(delta);
            if (!beginShoot) {
                curDeltaShoot = reloadDelta;
                beginShoot = true;
            }

            if (beginShoot && curDeltaShoot >= reloadDelta) {
                shoot();
                curDeltaShoot = 0;
            }
        } else {
            super.update(delta * acceleration);
        }

        if (getTop() < worldBounds.getBottom()) {
            destroy();
        }
    }

    public void set(EnemySettingsDto settings) {
        this.regions = settings.getRegions();
        this.v.set(settings.getV0());
        this.bulletRegion = settings.getBulletRegion();
        this.bulletHeight = settings.getBulletHeight();
        this.bulletV.set(settings.getBulletV());
        this.bulletSound = settings.getBulletSound();
        this.bulletDamage = settings.getDamage();
        this.reloadDelta = settings.getReloadInterval();
        setHeightProportion(settings.getHeight());
        this.hp = settings.getHp();
        this.acceleration = settings.getAcceleration();
    }

    @Override
    public void destroy() {
        super.destroy();
        beginShoot = false;
    }
}
