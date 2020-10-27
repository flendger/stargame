package ru.flendger.game;

import com.badlogic.gdx.Game;
import ru.flendger.game.screen.MenuScreen;

public class StarGame extends Game {
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
}
