package ru.sapeshkoas.dunegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ru.sapeshkoas.dunegame.core.Assets;

public class MenuScreen extends AbstractScreen {
    private Stage stage;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }


    @Override
    public void show() {
        menuGUI();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.4f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void menuGUI() {
        stage = new Stage(ScreenManager.getOurInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin();
        skin.addRegions(Assets.getOurInstance().getTextureAtlas());
        BitmapFont font24 = Assets.getOurInstance().getAssetManager().get("fonts/font24.ttf");
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                skin.getDrawable("simpleButton"), null, null, font24
        );
        final TextButton startGameBtn = new TextButton("Start Game", textButtonStyle);
        startGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getOurInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });
        final TextButton exitGameBtn = new TextButton("Exit from Game", textButtonStyle);
        exitGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        startGameBtn.setPosition(220, 20);
        exitGameBtn.setPosition(780, 20);
        stage.addActor(startGameBtn);
        stage.addActor(exitGameBtn);
        skin.dispose();
    }

    @Override
    public void dispose() {

    }
}
