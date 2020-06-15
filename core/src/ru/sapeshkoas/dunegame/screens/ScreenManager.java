package ru.sapeshkoas.dunegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.sapeshkoas.dunegame.DuneGame;
import ru.sapeshkoas.dunegame.screens.utils.Assets;

public class ScreenManager {
    public enum ScreenType {
        MENU, GAME
    }

    public final static int WORLD_WIDTH = 1280;
    public final static int HALF_WORLD_WIDTH = WORLD_WIDTH / 2;
    public final static int WORLD_HEIGHT = 720;
    public final static int HALF_WORLD_HEIGHT = WORLD_HEIGHT / 2;

    private DuneGame duneGame;
    private SpriteBatch batch;
    private GameScreen gameScreen;
    private LoadingScreen loadingScreen;
    private MenuScreen menuScreen;
    private Screen targetScreen;
    private Viewport viewport;
    private Camera camera;

    private static ScreenManager ourInstance = new ScreenManager();

    private ScreenManager() {

    }

    public static ScreenManager getOurInstance() {
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Camera getCamera() {
        return camera;
    }

    public void init(DuneGame game, SpriteBatch batch) {
        this.duneGame = game;
        this.batch = batch;
        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.gameScreen = new GameScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        this.menuScreen = new MenuScreen(batch);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public void resetCamera() {
        camera.position.set(HALF_WORLD_WIDTH, HALF_WORLD_HEIGHT, 0.0f);
        camera.update();
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
    }

    public void pointCameraTo(Vector2 point) {
        camera.position.set(point, 0.0f);
        camera.update();
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
    }

    public void changeScreen(ScreenType type) {
        Screen screen = duneGame.getScreen();
        Assets.getOurInstance().clear();
        Gdx.input.setInputProcessor(null);
        if (screen != null) {
            screen.dispose();
        }
        resetCamera();
        duneGame.setScreen(loadingScreen);
        switch (type) {
            case GAME:
                targetScreen = gameScreen;
                Assets.getOurInstance().loadAssets(ScreenType.GAME);
                break;
            case MENU:
                targetScreen = menuScreen;
                Assets.getOurInstance().loadAssets(ScreenType.MENU);
                break;
        }
    }

    public void goToTarget() {
        duneGame.setScreen(targetScreen);
    }
}
