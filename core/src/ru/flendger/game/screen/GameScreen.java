package ru.flendger.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import ru.flendger.game.base.BaseScreen;
import ru.flendger.game.base.Sprite;
import ru.flendger.game.math.Rect;
import ru.flendger.game.pool.BulletPool;
import ru.flendger.game.pool.EnemyShipPool;
import ru.flendger.game.sprite.Background;
import ru.flendger.game.sprite.Cosmo;
import ru.flendger.game.sprite.Star;
import ru.flendger.game.util.EnemyEmitter;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends BaseScreen {
    private static final int STAR_COUNT = 64;

    private BulletPool bulletPool;
    private EnemyShipPool enemyShipPool;
    private Cosmo cosmo;
    private EnemyEmitter enemyEmitter;

    private List<Disposable> toDispose;
    private List<Sprite> sprites;

    @Override
    public void show() {
        super.show();
        sprites = new ArrayList<>();
        toDispose = new ArrayList<>();

        TextureAtlas atlas = new TextureAtlas("textures/mainAtlas.tpack");
        toDispose.add(atlas);
        Texture bg = new Texture("textures/bg.png");
        toDispose.add(bg);

        sprites.add(new Background(bg));
        for (int i = 0; i < STAR_COUNT; i++) {
            sprites.add(new Star(atlas));
        }

        bulletPool = new BulletPool();
        toDispose.add(bulletPool);

        enemyShipPool = new EnemyShipPool(bulletPool, worldBounds);
        toDispose.add(enemyShipPool);

        enemyEmitter = new EnemyEmitter(atlas, enemyShipPool, worldBounds);
        toDispose.add(enemyEmitter);

        cosmo = new Cosmo(atlas, bulletPool, worldBounds);
        sprites.add(cosmo);
        toDispose.add(cosmo);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollision();
        freeAllDestroyed();
        draw();
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
        for (Sprite s: sprites
        ) {
            s.keyDown(keycode);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        for (Sprite s: sprites
        ) {
            s.keyUp(keycode);
        }
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
        bulletPool.updateActiveSprites(delta);
        enemyShipPool.updateActiveSprites(delta);
        enemyEmitter.generate(delta);
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyShipPool.freeAllDestroyedActiveSprites();
    }

    private void checkCollision() {
    }

    private void draw() {
        batch.begin();
        for (Sprite s: sprites
        ) {
            s.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        enemyShipPool.drawActiveSprites(batch);
        batch.end();
    }
}
