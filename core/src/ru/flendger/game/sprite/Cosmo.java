package ru.flendger.game.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.flendger.game.base.Sprite;
import ru.flendger.game.math.Rect;
import ru.flendger.game.pool.BulletPool;

public class Cosmo extends Sprite {
    private final float FIX_SPEED = 0.0055f;
    private final Vector2 v = new Vector2();

    private Rect worldBounds;
    private final BulletPool bulletPool;
    private final TextureRegion bulletRegion;
    private final int MAX_BULLETS = 15;
    private final float deltaShoot = 0.25f;
    private float curDeltaShoot = 0;
    private final Sound shootSound;

    private int curDirection = 0;
    private final int INVALID_POINTER = -1;
    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;
    private final Vector2 bulletV = new Vector2(0, 0.5f);
    private final Vector2 bulletPos = new Vector2();


    public Cosmo(TextureAtlas atlas, BulletPool bulletPool, Sound shootSound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.shootSound = shootSound;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.add(v);
        if (getLeft() <= worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        } else if (getRight() >= worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (bulletPool.getActiveObjects().size() < MAX_BULLETS) {
            curDeltaShoot += delta;
            if (curDeltaShoot >= deltaShoot) {
                shoot();
                curDeltaShoot = 0;
            }
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(0.2f * worldBounds.getHeight());
        pos.set(0, worldBounds.getBottom()+getHalfHeight());
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer == INVALID_POINTER) {
                leftPointer = pointer;
                moveLeft();
            }
        } else {
            if (rightPointer == INVALID_POINTER) {
                rightPointer = pointer;
                moveRight();
            }
        }
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (leftPointer == pointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer == INVALID_POINTER) {
                stop();
            }
        } else if (rightPointer == pointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer == INVALID_POINTER) {
                stop();
            }
        }
        return super.touchUp(touch, pointer, button);
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case (Input.Keys.LEFT):
                if (curDirection < 0) {
                    stop();
                }
                break;
            case (Input.Keys.RIGHT):
                if (curDirection > 0) {
                    stop();
                }
                break;
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case (Input.Keys.LEFT):
                moveLeft();
                break;
            case (Input.Keys.RIGHT):
                moveRight();
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }
        return super.keyDown(keycode);
    }

    private void moveLeft() {
        curDirection = -1;
        v.set(curDirection, 0).setLength(FIX_SPEED);
    }

    private void moveRight() {
        curDirection = 1;
        v.set(curDirection, 0).setLength(FIX_SPEED);
    }

    private void stop() {
        v.setZero();
        curDirection = 0;
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos.x, getTop());
        bullet.set(this, bulletRegion, bulletPos, bulletV, worldBounds, 1, 0.01f);
        shootSound.play();
    }

}
