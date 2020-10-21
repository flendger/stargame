package ru.flendger.game.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.flendger.game.base.Sprite;
import ru.flendger.game.math.Rect;

public class Cosmo extends Sprite {
    private final float FIX_SPEED = 0.0025f;
    private final Vector2 v;
    private final Vector2 lastTouch;
    private final Vector2 tmp;

    public Cosmo(TextureRegion region) {
        super(region);
        this.v = new Vector2();
        this.lastTouch = new Vector2();
        this.tmp = new Vector2();
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        tmp.set(lastTouch);
        if (tmp.sub(pos).len() <= v.len()) {
            pos.set(lastTouch);
            v.setZero();
        } else {
            pos.add(v);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.2f * worldBounds.getHeight());
        pos.set(0, worldBounds.getBottom()+getHalfHeight());
        lastTouch.set(pos);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        lastTouch.set(touch);
        v.set((touch.cpy().sub(pos)).setLength(FIX_SPEED));
        return super.touchDown(touch, pointer, button);
    }
}
