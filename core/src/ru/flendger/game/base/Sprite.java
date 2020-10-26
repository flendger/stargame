package ru.flendger.game.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.flendger.game.math.Rect;

public class Sprite extends Rect {
    protected float angle;
    protected float scale = 1;
    protected TextureRegion[] regions;
    protected int frame;

    public Sprite(TextureRegion region) {
        this.regions = new TextureRegion[1];
        this.frame = 0;
        this.regions[frame] = region;
    }

    public Sprite(TextureRegion region, int devider) {
        this.regions = new TextureRegion[devider];
        for (int i = 0; i < devider; i++) {
            this.regions[i] = new TextureRegion(region, region.getRegionWidth()- region.getRegionWidth()/(i+1), 0, region.getRegionWidth()/devider, region.getRegionHeight());
        }
        this.frame = devider-1;
    }

    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                halfWidth, halfHeight,
                getWidth(), getHeight(),
                scale, scale,
                angle
                );
    }

    public void update(float delta) {
    }

    public void resize(Rect worldBounds) {
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
