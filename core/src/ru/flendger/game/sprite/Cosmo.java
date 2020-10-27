package ru.flendger.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.flendger.game.base.Sprite;
import ru.flendger.game.math.Rect;

public class Cosmo extends Sprite {
    private final float FIX_SPEED = 0.0035f;
    private final float FIX_MOVE = 0.0400f;
    private final Vector2 v;
    private final Vector2 lastTouch;
    private final Vector2 tmp;
    private Rect worldBounds;

    public Cosmo(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"), 2);

        this.v = new Vector2();
        this.lastTouch = new Vector2();
        this.tmp = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        tmp.set(lastTouch);
        if (tmp.sub(pos).len() <= FIX_SPEED) {
            pos.set(lastTouch);
            v.setZero();
        } else {
            pos.add(v);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(0.2f * worldBounds.getHeight());
        pos.set(0, worldBounds.getBottom()+getHalfHeight());
        lastTouch.set(pos);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x <= 0) {
            moveLeft();
        } else {
            moveRight();
        }
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case (21):
                moveLeft();
                break;
            case (22):
                moveRight();
                break;
        }
        return super.keyDown(keycode);
    }

    private void moveLeft() {
        float x = Math.max((pos.x - FIX_MOVE), (worldBounds.getLeft() + getHalfWidth()));
        lastTouch.set(x, pos.y);
        v.set((lastTouch.cpy().sub(pos)).setLength(FIX_SPEED));
    }

    private void moveRight() {
        float x = Math.min((pos.x + FIX_MOVE), (worldBounds.getRight() - getHalfWidth()));
        lastTouch.set(x, pos.y);
        v.set((lastTouch.cpy().sub(pos)).setLength(FIX_SPEED));
    }
}
