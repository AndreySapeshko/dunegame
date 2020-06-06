package ru.sapeshkoas.dunegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.sapeshkoas.dunegame.screens.GameScreen;
import ru.sapeshkoas.dunegame.screens.ScreenManager;

public class DuneGame extends Game {
	private SpriteBatch batch;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		ScreenManager.getOurInstance().init(this, batch);
		ScreenManager.getOurInstance().changeScreen(ScreenManager.ScreenType.GAME);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float dt = Gdx.graphics.getDeltaTime();
		getScreen().render(dt);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
