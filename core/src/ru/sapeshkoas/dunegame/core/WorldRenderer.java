package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import ru.sapeshkoas.dunegame.core.units.BattleTank;
import ru.sapeshkoas.dunegame.screens.ScreenManager;
import ru.sapeshkoas.dunegame.screens.utils.Assets;

public class WorldRenderer {
    private SpriteBatch batch;
    private GameController gc;
    private BitmapFont font32;
    private TextureRegion selectorTexture;

    private FrameBuffer frameBuffer;
    private TextureRegion frameBufferRegion;
    private ShaderProgram shaderProgram;

    public WorldRenderer(SpriteBatch batch, GameController gc) {
        this.batch = batch;
        this.gc = gc;
        this.font32 = Assets.getOurInstance().getAssetManager().get("fonts/font32.ttf");
        this.selectorTexture = Assets.getOurInstance().getTextureAtlas().findRegion("selector");
        this.frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, ScreenManager.WORLD_WIDTH, ScreenManager.WORLD_HEIGHT, false);
        this.frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        this.frameBufferRegion.flip(false, true);
        this.shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/vertex.glsl").readString(), Gdx.files.internal("shaders/fragment.glsl").readString());
        if (!shaderProgram.isCompiled()) {
            throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());
        }
    }

    public void render() {
        ScreenManager.getOurInstance().pointCameraTo(gc.getPointOfView());
        frameBuffer.begin();
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        gc.getBattleMap().render(batch);
        gc.getUnitsController().render(batch);
        gc.getBuildingController().render(batch);
        gc.getProjectileController().render(batch);
        gc.getParticleController().render(batch);
        drawSelectionFrame();
        batch.end();
        frameBuffer.end();

        ScreenManager.getOurInstance().resetCamera();
        batch.begin();
        batch.setShader(shaderProgram);
        shaderProgram.setUniformf(shaderProgram.getUniformLocation("time"), gc.getWorldTimer());
        shaderProgram.setUniformf(shaderProgram.getUniformLocation("px"), gc.getPointOfView().x / ScreenManager.WORLD_WIDTH);
        shaderProgram.setUniformf(shaderProgram.getUniformLocation("py"), gc.getPointOfView().y / ScreenManager.WORLD_HEIGHT);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.draw(frameBufferRegion, 0.0f, 0.0f);
        batch.end();
        batch.setShader(null);

        ScreenManager.getOurInstance().resetCamera();
        gc.getStage().draw();

        if (gc.isPaused()) {
            batch.begin();
            font32.draw(batch, "PAUSED", 0.0f,350, ScreenManager.WORLD_WIDTH, 1, false);
            batch.end();
        }
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
