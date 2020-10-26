package ru.flendger.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import ru.flendger.game.base.BaseScreen;
import ru.flendger.game.base.Sprite;
import ru.flendger.game.math.Rect;
import ru.flendger.game.sprite.Background;
import ru.flendger.game.sprite.Cosmo;
import ru.flendger.game.sprite.Star;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private TextureAtlas atlas;
    private Texture bg;

    private List<Disposable> toDispose;
    private List<Sprite> sprites;

    @Override
    public void show() {
        super.show();
        sprites = new ArrayList<>();
        toDispose = new ArrayList<>();

        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        toDispose.add(atlas);
        bg = new Texture("textures/bg.png");
        toDispose.add(bg);

        sprites.add(new Background(bg));
        for (int i = 0; i < STAR_COUNT; i++) {
            sprites.add(new Star(atlas));
        }

        sprites.add(new Cosmo(atlas));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollision();
        draw(delta);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
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
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        for (Sprite s: sprites
        ) {
            s.touchDown(touch, pointer, button);
        }
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        for (Sprite s: sprites
        ) {
            s.touchUp(touch, pointer, button);
        }
        return super.touchUp(touch, pointer, button);
    }

    private void update(float delta) {
        for (Sprite s: sprites
        ) {
            s.update(delta);
        }
    }

    private void checkCollision() {

    }

    private void draw(float delta) {
        batch.begin();
        for (Sprite s: sprites
        ) {
            s.draw(batch);
        }
        batch.end();
    }
}
