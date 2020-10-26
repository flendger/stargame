package ru.flendger.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import ru.flendger.game.base.BaseScreen;
import ru.flendger.game.base.Sprite;
import ru.flendger.game.math.Rect;
import ru.flendger.game.sprite.Background;
import ru.flendger.game.sprite.ExitButton;
import ru.flendger.game.sprite.PlayButton;
import ru.flendger.game.sprite.Star;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends BaseScreen {
    private static int STAR_COUNTER = 128;
    private final Game game;

    private Texture bg;
    private TextureAtlas atlas;

    private List<Disposable> toDispose;
    private List<Sprite> sprites;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        toDispose = new ArrayList<>();
        sprites = new ArrayList<>();

        bg = new Texture("textures/bg.png");
        toDispose.add(bg);
        sprites.add(new Background(bg));

        atlas = new TextureAtlas("textures/menuAtlas.tpack");
        toDispose.add(atlas);

        sprites.add(new PlayButton(atlas, game));
        sprites.add(new ExitButton(atlas));

        for (int i = 0; i < STAR_COUNTER; i++) {
            sprites.add(new Star(atlas));
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw(delta);
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

    private void draw(float delta) {
        batch.begin();
        for (Sprite s: sprites
        ) {
            s.draw(batch);
        }
        batch.end();
    }
}
