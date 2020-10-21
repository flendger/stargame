package ru.flendger.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.flendger.game.base.BaseScreen;
import ru.flendger.game.math.Rect;
import ru.flendger.game.sprite.Background;
import ru.flendger.game.sprite.Cosmo;

public class MenuScreen extends BaseScreen {
    private Background background;
    private Texture bg;
    private Cosmo cosmo;
    private Texture cosmoImg;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/background.jpg");
        background = new Background(new TextureRegion(bg));
        cosmoImg = new Texture("textures/cosmo.png");
        cosmo = new Cosmo(new TextureRegion(cosmoImg));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        cosmo.draw(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        cosmo.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        cosmoImg.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        cosmo.touchDown(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }
}
