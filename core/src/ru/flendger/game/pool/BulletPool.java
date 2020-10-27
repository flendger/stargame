package ru.flendger.game.pool;

import ru.flendger.game.base.SpritesPool;
import ru.flendger.game.sprite.Bullet;

public class BulletPool extends SpritesPool<Bullet> {
    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
