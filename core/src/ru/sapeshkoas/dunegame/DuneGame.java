package ru.sapeshkoas.dunegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.sapeshkoas.dunegame.screens.GameScreen;

public class DuneGame extends Game {
	private SpriteBatch batch;
	private GameScreen gameScreen;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.gameScreen = new GameScreen(batch);
		this.setScreen(gameScreen);
	}

	@Override
	public void render () {
		getScreen().render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
