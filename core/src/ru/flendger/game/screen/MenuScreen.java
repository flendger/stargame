package ru.flendger.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import ru.flendger.game.base.BaseScreen;
import ru.flendger.game.base.Sprite;
import ru.flendger.game.math.Rect;
import ru.flendger.game.sprite.Background;
import ru.flendger.game.sprite.Cosmo;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends BaseScreen {
    private Texture bg;
    private Texture cosmoImg;
    private List<Disposable> toDispose;
    private List<Sprite> sprites;

    @Override
    public void show() {
        super.show();
        toDispose = new ArrayList<>();
        sprites = new ArrayList<>();

        bg = new Texture("textures/background.jpg");
        toDispose.add(bg);
        sprites.add(new Background(new TextureRegion(bg)));

        cosmoImg = new Texture("textures/cosmo.png");
        toDispose.add(cosmoImg);
        sprites.add(new Cosmo(new TextureRegion(cosmoImg)));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        for (Sprite s: sprites
             ) {
            s.draw(batch);
        }
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        for (Sprite s: sprites
             ) {
            s.resize(worldBounds);
        }
    }

    @Override
    public void dispose() {
        for (Disposable d: toDispose
             ) {
            d.dispose();
        }
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        for (Sprite s: sprites
             ) {
            s.touchDown(touch, pointer, button);
        }
        return super.touchDown(touch, pointer, button);
    }
}
