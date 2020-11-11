package ru.flendger.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.flendger.game.base.BaseShip;
import ru.flendger.game.math.Rect;
import ru.flendger.game.pool.BulletPool;
import ru.flendger.game.pool.ExplosionPool;

public class Cosmo extends BaseShip {

    private final float FIX_SPEED = 0.5f;
    private final int INVALID_POINTER = -1;

    private int curDirection;
    private int leftPointer;
    private int rightPointer;


    public Cosmo(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds) {
        super(atlas.findRegion("main_ship"),
                1, 2, 2,
                bulletPool,
                explosionPool,
                atlas.findRegion("bulletMainShip"),
                Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav")),
                worldBounds);
        loadDefaults();
    }

    @Override
    public void update(float delta) {
        bulletPos.set(pos.x, getTop());
        super.update(delta);

        if (getLeft() <= worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        } else if (getRight() >= worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.15f * worldBounds.getHeight());
        setStartPosition();
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

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                        || bullet.getLeft() > getRight()
                        || bullet.getBottom() > pos.y
                        || bullet.getTop() < getBottom()
        );
    }

    private void setStartPosition() {
        pos.set(0, worldBounds.getBottom()+getHalfHeight());
    }

    public Vector2 getV() {
        return v;
    }

    @Override
    public void loadDefaults() {
        super.loadDefaults();
        curDirection = 0;
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;
        v.setZero();
        reloadDelta = 0.25f;
        bulletV.set(0, 0.5f);
        bulletDamage = 1;
        bulletHeight = 0.01f;
        hp = 3;
        setStartPosition();
        flushDestroy();
    }
}
