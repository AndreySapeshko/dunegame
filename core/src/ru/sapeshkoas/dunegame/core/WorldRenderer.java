package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.sapeshkoas.dunegame.core.GameController;

public class WorldRenderer {
    private SpriteBatch batch;
    private GameController gc;
    private BitmapFont font32;

    public WorldRenderer(SpriteBatch batch, GameController gc) {
        this.batch = batch;
        this.gc = gc;
        this.font32 = Assets.getOurInstance().getAssetManager().get("fonts/font32.ttf");
    }

    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        gc.getBattleMap().render(batch);
        gc.getTanksController().render(batch);
        gc.getProjectileController().render(batch);
        font32.draw(batch, "Dune Game 2020", 0, 680, 1280, 1, false);
        batch.end();
    }
}
