package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.sapeshkoas.dunegame.core.GameController;
import ru.sapeshkoas.dunegame.screens.ScreenManager;

public class WorldRenderer {
    private SpriteBatch batch;
    private GameController gc;
    private BitmapFont font32;
    private TextureRegion selectorTexture;

    public WorldRenderer(SpriteBatch batch, GameController gc) {
        this.batch = batch;
        this.gc = gc;
        this.font32 = Assets.getOurInstance().getAssetManager().get("fonts/font32.ttf");
        this.selectorTexture = Assets.getOurInstance().getTextureAtlas().findRegion("selector");
    }

    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenManager.getOurInstance().pointCameraTo(gc.getPointOfView());
        batch.begin();
        gc.getBattleMap().render(batch);
        gc.getUnitsController().render(batch);
        gc.getProjectileController().render(batch);
        gc.getParticleController().render(batch);
        drawSelectionFrame();
//        font32.draw(batch, "Dune Game 2020", 0, 680, 1280, 1, false);
        batch.end();
        ScreenManager.getOurInstance().resetCamera();
        gc.getStage().draw();
    }

    public void drawSelectionFrame() {
        if (gc.getSelectionStart().x > 0 && gc.getSelectionStart().y > 0) {
            batch.draw(selectorTexture, gc.getMouse().x - 8, gc.getMouse().y - 8);
            batch.draw(selectorTexture, gc.getMouse().x - 8, gc.getSelectionStart().y - 8);
            batch.draw(selectorTexture, gc.getSelectionStart().x - 8, gc.getMouse().y - 8);
            batch.draw(selectorTexture, gc.getSelectionStart().x - 8, gc.getSelectionStart().y - 8);
            float startX = Math.min(gc.getMouse().x, gc.getSelectionStart().x);
            float endX = Math.max(gc.getMouse().x, gc.getSelectionStart().x);
            float startY = Math.min(gc.getMouse().y, gc.getSelectionStart().y);
            float endY = Math.max(gc.getMouse().y, gc.getSelectionStart().y);
            for (float x = startX; x < endX; x += 20) {
                batch.draw(selectorTexture, x - 4, startY - 4, 8, 8);
                batch.draw(selectorTexture, x - 4, endY - 4, 8, 8);
            }
            for (float y = startY; y < endY; y += 20) {
                batch.draw(selectorTexture, startX - 4, y - 4, 8, 8);
                batch.draw(selectorTexture, endX - 4, y - 4, 8, 8);
            }
        }
    }
}
