package ru.flendger.game.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import ru.flendger.game.math.Rect;
import ru.flendger.game.pool.BulletPool;
import ru.flendger.game.sprite.Bullet;

public class BaseShip extends Sprite implements Disposable {
    protected final Rect worldBounds;
    protected final Vector2 v = new Vector2();
    protected final BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected float reloadDelta = 0;
    protected float curDeltaShoot = 0;
    protected int bulletDamage = 0;
    protected float bulletHeight = 0;
    protected Sound bulletSound;
    protected final Vector2 bulletV = new Vector2();
    protected final Vector2 bulletPos = new Vector2();
    protected int hp = 0;

    public BaseShip(BulletPool bulletPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
    }

    public BaseShip(TextureRegion region, int rows, int cols, int frames, BulletPool bulletPool, TextureRegion bulletRegion, Sound bulletSound, Rect worldBounds) {
        super(region, rows, cols, frames);
        this.bulletPool = bulletPool;
        this.bulletRegion = bulletRegion;
        this.bulletSound = bulletSound;
        this.worldBounds = worldBounds;
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
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void dispose() {
        bulletSound.dispose();
    }
}
