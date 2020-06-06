package ru.sapeshkoas.dunegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen extends AbstractScreen {

    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.4f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void update(float dt) {
        if (Gdx.input.justTouched()) {
            ScreenManager.getOurInstance().changeScreen(ScreenManager.ScreenType.GAME);
        }
    }

    @Override
    public void dispose() {

    }
}
