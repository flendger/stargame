package ru.flendger.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.flendger.game.base.Sprite;
import ru.flendger.game.math.Rect;

public class GameOverMessage extends Sprite {

    public GameOverMessage(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.10f);
        pos.set(0, 0);
    }
}
