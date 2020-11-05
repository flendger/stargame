package ru.flendger.game.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import ru.flendger.game.math.Rect;
import ru.flendger.game.pool.BulletPool;
import ru.flendger.game.pool.ExplosionPool;
import ru.flendger.game.sprite.Bullet;
import ru.flendger.game.sprite.Explosion;

public class BaseShip extends Sprite implements Disposable {

    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;

    protected final Rect worldBounds;
    protected final BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    private final ExplosionPool explosionPool;
    protected Sound bulletSound;

    protected final Vector2 v = new Vector2();
    protected final Vector2 v0 = new Vector2();
    protected float reloadDelta;
    protected float curDeltaShoot;
    protected int bulletDamage;
    protected float bulletHeight;
    protected final Vector2 bulletV = new Vector2();
    protected final Vector2 bulletPos = new Vector2();
    protected int hp;
    private float damageAnimateTimer;


    public BaseShip(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.explosionPool = explosionPool;
        loadDefaults();
    }

    public BaseShip(TextureRegion region, int rows, int cols, int frames, BulletPool bulletPool, ExplosionPool explosionPool, TextureRegion bulletRegion, Sound bulletSound, Rect worldBounds) {
        super(region, rows, cols, frames);
        this.bulletPool = bulletPool;
        this.bulletRegion = bulletRegion;
        this.bulletSound = bulletSound;
        this.worldBounds = worldBounds;
        this.explosionPool = explosionPool;
        loadDefaults();
    }

    public int getHp() {
        return hp;
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, worldBounds, bulletDamage, bulletHeight);
        bulletSound.play();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        curDeltaShoot += delta;
        if (curDeltaShoot > reloadDelta) {
            shoot();
            curDeltaShoot = 0f;
        }

        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    public void damage(int damage) {
        frame = 1;
        damageAnimateTimer = 0f;

        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            destroy();
        }
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void dispose() {
        bulletSound.dispose();
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    private void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }

    public void loadDefaults() {
        reloadDelta = 0;
        curDeltaShoot = 0;
        bulletDamage = 0;
        bulletHeight = 0;
        bulletV.setZero();
        bulletPos.setZero();
        v.setZero();
        v0.setZero();
        hp = 0;
        damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;
    }
}
