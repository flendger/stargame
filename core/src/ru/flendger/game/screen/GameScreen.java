package ru.flendger.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import ru.flendger.game.base.BaseScreen;
import ru.flendger.game.base.Sprite;
import ru.flendger.game.math.Rect;
import ru.flendger.game.pool.BulletPool;
import ru.flendger.game.pool.EnemyShipPool;
import ru.flendger.game.pool.ExplosionPool;
import ru.flendger.game.sprite.*;
import ru.flendger.game.util.EnemyEmitter;
import ru.flendger.game.util.Font;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends BaseScreen {
    private static final int STAR_COUNT = 64;
    private static final float EXIT_INTERVAL = 1f;

    private static final float FONT_SIZE = 0.02f;
    private static final float MARGIN = 0.01f;

    private static final String FRAGS = "FRAGS: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "LEVEL: ";

    private BulletPool bulletPool;
    private EnemyShipPool enemyShipPool;
    private ExplosionPool explosionPool;
    private Cosmo cosmo;
    private EnemyEmitter enemyEmitter;
    private GameOverMessage gameOverMessage;
    private NewGameButton newGameButton;

    private final List<Disposable> toDispose;
    private final List<Sprite> sprites;

    private float exitTimer;

    private int frags;

    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;

    public GameScreen() {
        this.sprites = new ArrayList<>();
        this.toDispose = new ArrayList<>();
    }

    @Override
    public void show() {
        super.show();

        TextureAtlas atlas = new TextureAtlas("textures/mainAtlas.tpack");
        toDispose.add(atlas);
        Texture bg = new Texture("textures/bg.png");
        toDispose.add(bg);

        sprites.add(new Background(bg));

        bulletPool = new BulletPool();
        toDispose.add(bulletPool);

        explosionPool = new ExplosionPool(atlas);
        toDispose.add(explosionPool);

        enemyShipPool = new EnemyShipPool(bulletPool, explosionPool, worldBounds);
        toDispose.add(enemyShipPool);

        enemyEmitter = new EnemyEmitter(atlas, enemyShipPool, worldBounds);
        toDispose.add(enemyEmitter);

        cosmo = new Cosmo(atlas, bulletPool, explosionPool, worldBounds);
        sprites.add(cosmo);
        toDispose.add(cosmo);

        for (int i = 0; i < STAR_COUNT; i++) {
            sprites.add(new TrackingStar(atlas, cosmo.getV()));
//            sprites.add(new Star(atlas));
        }

        gameOverMessage = new GameOverMessage(atlas);
        gameOverMessage.destroy();
        sprites.add(gameOverMessage);

        newGameButton = new NewGameButton(atlas, this);
        newGameButton.destroy();
        sprites.add(newGameButton);

        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
    }

    public void startGame() {
        gameOverMessage.destroy();
        newGameButton.destroy();
        cosmo.loadDefaults();
        exitTimer = 0f;
        frags = 0;
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
        toDispose.clear();
    }

    @Override
    public boolean keyDown(int keycode) {
        for (Sprite s: sprites
        ) {
            if (!s.isDestroyed()) {
                s.keyDown(keycode);
            }
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        for (Sprite s: sprites
        ) {
            if (!s.isDestroyed()) {
                s.keyUp(keycode);
            }
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        for (Sprite s: sprites
        ) {
            if (!s.isDestroyed()) {
                s.touchDown(touch, pointer, button);
            }
        }
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        for (Sprite s: sprites
        ) {
            if (!s.isDestroyed()) {
                s.touchUp(touch, pointer, button);
            }
        }
        return super.touchUp(touch, pointer, button);
    }

    private void update(float delta) {
        for (Sprite s: sprites
        ) {
            if (!s.isDestroyed()) {
                s.update(delta);
            }
        }
        enemyShipPool.updateActiveSprites(delta);
        if (cosmo.getHp() <= 0) {
            enemyShipPool.dispose();
            bulletPool.dispose();
            exitTimer += delta;
            if (exitTimer >= EXIT_INTERVAL) {
                gameOverMessage.flushDestroy();
                newGameButton.flushDestroy();
            }
        } else {
            enemyEmitter.generate(delta, frags);
        }
        bulletPool.updateActiveSprites(delta);
        explosionPool.updateActiveSprites(delta);
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyShipPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
    }

    private void checkCollision() {
        List<EnemyShip> enemyShips = enemyShipPool.getActiveObjects();
        for (EnemyShip enemyShip: enemyShips
             ) {
            if (enemyShip.isDestroyed()) {
                continue;
            }

            float minDist = cosmo.getHalfWidth() + enemyShip.getHalfWidth();
            if (enemyShip.pos.dst(cosmo.pos) < minDist) {
                cosmo.damage(enemyShip.getBulletDamage());
                enemyShip.destroy();
            }
        }

        List<Bullet> bullets = bulletPool.getActiveObjects();
        for (Bullet bullet: bullets
             ) {
            if (bullet.isDestroyed()) {
                continue;
            }

            if (bullet.getOwner() != cosmo) {
                if (cosmo.isBulletCollision(bullet)) {
                    bullet.destroy();
                    cosmo.damage(bullet.getDamage());
                }
            } else {
                for (EnemyShip enemyShip: enemyShips
                     ) {
                    if (enemyShip.isDestroyed()) {
                        continue;
                    }

                    if (enemyShip.isBulletCollision(bullet)){
                        bullet.destroy();
                        enemyShip.damage(cosmo.getBulletDamage());
                        if (enemyShip.isDestroyed()) {
                            frags ++;
                        }
                        break;
                    }
                }
            }
        }
    }

    private void draw() {
        batch.begin();
        for (Sprite s: sprites
        ) {
            if (!s.isDestroyed()) {
                s.draw(batch);
            }
        }
        enemyShipPool.drawActiveSprites(batch);
        bulletPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + MARGIN, worldBounds.getTop() - MARGIN);
        sbHp.setLength(0);
        font.draw(batch, sbHp.append(HP).append(cosmo.getHp()), worldBounds.pos.x, worldBounds.getTop() - MARGIN, Align.center);
        sbLevel.setLength(0);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - MARGIN, worldBounds.getTop() - MARGIN, Align.right);
    }
}
