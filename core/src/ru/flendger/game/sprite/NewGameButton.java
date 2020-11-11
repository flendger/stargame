package ru.flendger.game.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.flendger.game.base.BaseButton;
import ru.flendger.game.math.Rect;
import ru.flendger.game.screen.GameScreen;

public class NewGameButton extends BaseButton {
    private static final float MARGIN = 0.025f;

    private final GameScreen gameScreen;

    public NewGameButton(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
    }

    @Override
    public void action() {
        gameScreen.startGame();
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            action();
        }
        return super.keyUp(keycode);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setLeft(-getHalfWidth());
        setBottom(worldBounds.getBottom() + MARGIN);
    }
}
