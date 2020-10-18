package ru.flendger.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.flendger.game.base.BaseScreen;

public class MenuScreen extends BaseScreen {
    private Texture img;
    private Texture cosmoImg;
    private Vector2 pos = new Vector2(100, 0);
    private Vector2 lastTouch = new Vector2(0, 0);
    private Vector2 v = new Vector2(0, 0);
    private int scrWidth;
    private int scrHeight;
    private float xCosmo;
    private float yCosmo;
    private float sclSpeed = 5;

    @Override
    public void show() {
        super.show();
        img = new Texture("background.jpg");
        cosmoImg = new Texture("cosmo.png");
        scrWidth = Gdx.graphics.getWidth();
        scrHeight = Gdx.graphics.getHeight();
        xCosmo = scrWidth/5.4f;
        yCosmo = scrHeight/6;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        batch.draw(img, 0, 0, scrWidth, scrHeight);
        batch.draw(cosmoImg, pos.x, pos.y, xCosmo, yCosmo);
        batch.end();

        if (v.isZero()) {
            return;
        }

        pos.add(v);
        Vector2 distance = new Vector2(lastTouch);
        distance.sub(pos);
        if (distance.len() <= sclSpeed) {
            v.setZero();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
        cosmoImg.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastTouch.set(screenX, Gdx.graphics.getHeight()-screenY);
        lastTouch.sub(xCosmo/2, yCosmo/2);
        v.set(lastTouch);
        v.sub(pos);
        v.nor();
        v.scl(sclSpeed);

        return super.touchDown(screenX, screenY, pointer, button);
    }
}
