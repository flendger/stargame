package ru.flendger.game.pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.flendger.game.base.SpritesPool;
import ru.flendger.game.sprite.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {
    private final TextureAtlas atlas;
    private final Sound explosionSound;

    public ExplosionPool(TextureAtlas atlas) {
        this.atlas = atlas;
        this.explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(atlas.findRegion("explosion"), 9, 9, 74, explosionSound);
    }

    @Override
    public void dispose() {
        super.dispose();
        explosionSound.dispose();
    }
}
