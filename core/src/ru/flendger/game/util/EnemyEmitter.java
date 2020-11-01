package ru.flendger.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import ru.flendger.game.base.EnemySettingsDto;
import ru.flendger.game.dto.EnemyBigSettingsDto;
import ru.flendger.game.dto.EnemyMediumSettingsDto;
import ru.flendger.game.dto.EnemySmallSettingsDto;
import ru.flendger.game.math.Rect;
import ru.flendger.game.math.Rnd;
import ru.flendger.game.pool.EnemyShipPool;
import ru.flendger.game.sprite.EnemyShip;

public class EnemyEmitter implements Disposable {

    private static final float GENERATE_INTERVAL = 4f;

    private final Rect worldBounds;
    private final EnemyShipPool enemyShipPool;
    private float generateTimer;
    private final Sound bulletSound;

    private final EnemySettingsDto enemySmallSettingsDto;
    private final EnemySettingsDto enemyMediumSettingsDto;
    private final EnemySettingsDto enemyBigSettingsDto;

    public EnemyEmitter(TextureAtlas atlas, EnemyShipPool enemyShipPool, Rect worldBounds) {
        this.enemyShipPool = enemyShipPool;
        this.bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        this.worldBounds = worldBounds;

        enemySmallSettingsDto = new EnemySmallSettingsDto(atlas, bulletSound);
        enemyMediumSettingsDto = new EnemyMediumSettingsDto(atlas, bulletSound);
        enemyBigSettingsDto = new EnemyBigSettingsDto(atlas, bulletSound);
    }

    public void generate(float delta) {
        generateTimer += delta;
        if (generateTimer >= GENERATE_INTERVAL) {
            generateTimer = 0;
            EnemyShip enemyShip = enemyShipPool.obtain();
            float type = (float) Math.random();
            if (type < 0.5f) {
                enemyShip.set(enemySmallSettingsDto);
            } else if (type < 0.8f) {
                enemyShip.set(enemyMediumSettingsDto);
            } else {
                enemyShip.set(enemyBigSettingsDto);
            }

            enemyShip.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemyShip.getHalfWidth(), worldBounds.getRight() - enemyShip.getHalfWidth());
            enemyShip.setBottom(worldBounds.getTop());
        }
    }

    @Override
    public void dispose() {
        bulletSound.dispose();
    }
}
