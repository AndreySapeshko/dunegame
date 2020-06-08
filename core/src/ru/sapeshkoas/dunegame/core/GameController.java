package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ru.sapeshkoas.dunegame.core.gui.GuiPlayerInfo;
import ru.sapeshkoas.dunegame.core.units.AbstractUnit;
import ru.sapeshkoas.dunegame.core.units.BattleTank;
import ru.sapeshkoas.dunegame.core.units.Owner;
import ru.sapeshkoas.dunegame.screens.ScreenManager;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private final static float CAMERA_SPEED = 240.0f;

    private BattleMap map;
    private PlayerLogic playerLogic;
    private AiLogic aiLogic;
    private UnitsController unitsController;
    private ProjectileController projectileController;
    private ParticleController particleController;
    private Vector2 tmp;
    private Vector2 mouse;
    private Vector2 selectionStart;
    private Vector2 selectionEnd;
    private List<AbstractUnit> selectedUnits;
    private Collider collider;
    private Stage stage;
    private GuiPlayerInfo guiPlayerInfo;
    private Vector2 pointOfView;

    public Vector2 getSelectionStart() {
        return selectionStart;
    }

    public Vector2 getSelectionEnd() {
        return selectionEnd;
    }

    public ProjectileController getProjectileController() {
        return projectileController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public Stage getStage() {
        return stage;
    }

    public BattleMap getBattleMap() {
        return map;
    }

    public UnitsController getUnitsController() {
        return unitsController;
    }

    public List<AbstractUnit> getSelectedUnits() {
        return selectedUnits;
    }

    public Vector2 getMouse() {
        return mouse;
    }

    public Vector2 getPointOfView() {
        return pointOfView;
    }

    public GameController() {
        this.mouse = new Vector2();
        this.map = new BattleMap();
        this.collider = new Collider(this);
        this.playerLogic = new PlayerLogic(this);
        this.aiLogic = new AiLogic(this);
        this.tmp = new Vector2();
        this.selectionStart = new Vector2();
        this.selectedUnits = new ArrayList<>();
        this.unitsController = new UnitsController(this);
        this.projectileController = new ProjectileController();
        this.particleController = new ParticleController();
        this.pointOfView = new Vector2(ScreenManager.HALF_WORLD_WIDTH, ScreenManager.HALF_WORLD_HEIGHT);
        createGUIAndPrepareGameInput();
    }

    public boolean isUnitSelected(AbstractUnit abstractUnint) {
        return selectedUnits.contains(abstractUnint);
    }

    public void update(float dt) {
        ScreenManager.getOurInstance().pointCameraTo(pointOfView);
        mouse.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getOurInstance().getViewport().unproject(mouse);
        unitsController.update(dt);
        playerLogic.update(dt);
        aiLogic.update(dt);
        map.update(dt);
        projectileController.update(dt);
        particleController.update(dt);
        collider.checkCollision(dt);
        guiPlayerInfo.update(dt);
        ScreenManager.getOurInstance().resetCamera();
        stage.act(dt);
        changePOV(dt);
    }

    public void changePOV(float dt) {
        if (Gdx.input.getY() < 20) {
            pointOfView.y += CAMERA_SPEED * dt;
            if (pointOfView.y + ScreenManager.HALF_WORLD_HEIGHT > BattleMap.MAP_HEIGHT_PX) {
                pointOfView.y = BattleMap.MAP_HEIGHT_PX - ScreenManager.HALF_WORLD_HEIGHT;
            }
            ScreenManager.getOurInstance().pointCameraTo(pointOfView);
        }
        if (Gdx.input.getY() > 700) {
            pointOfView.y -= CAMERA_SPEED * dt;
            if (pointOfView.y < ScreenManager.HALF_WORLD_HEIGHT) {
                pointOfView.y = ScreenManager.HALF_WORLD_HEIGHT;
            }
            ScreenManager.getOurInstance().pointCameraTo(pointOfView);
        }
        if (Gdx.input.getX() < 20) {
            pointOfView.x -= CAMERA_SPEED * dt;
            if (pointOfView.x < ScreenManager.HALF_WORLD_WIDTH) {
                pointOfView.x = ScreenManager.HALF_WORLD_WIDTH;
            }
            ScreenManager.getOurInstance().pointCameraTo(pointOfView);
        }
        if (Gdx.input.getX() > 1260) {
            pointOfView.x += CAMERA_SPEED * dt;
            if (pointOfView.x + ScreenManager.HALF_WORLD_WIDTH > BattleMap.MAP_WIDTH_PX) {
                pointOfView.x = BattleMap.MAP_WIDTH_PX - ScreenManager.HALF_WORLD_WIDTH;
            }
            ScreenManager.getOurInstance().pointCameraTo(pointOfView);
        }
    }

    public InputProcessor prepareInput() {
        return new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    selectionStart.set(mouse);
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    tmp.set(mouse);
                    if (tmp.x < selectionStart.x) {
                        float buf = tmp.x;
                        tmp.x = selectionStart.x;
                        selectionStart.x = buf;
                    }
                    if (tmp.y > selectionStart.y) {
                        float buf = tmp.y;
                        tmp.y = selectionStart.y;
                        selectionStart.y = buf;
                    }
                    selectedUnits.clear();
                    if (Math.abs(tmp.x - selectionStart.x) > 20 && Math.abs(tmp.y - selectionStart.y) > 20) {
                        for (int i = 0; i < unitsController.getPlayerUnits().size(); i++ ) {
                            AbstractUnit t = unitsController.getPlayerUnits().get(i);
                            if (t.position.x < tmp.x && t.position.y < selectionStart.y &&
                                    t.position.y > tmp.y && t.position.x > selectionStart.x) {
                                selectedUnits.add(t);
                            }
                        }
                    } else {
                        for (int i = 0; i < unitsController.getPlayerUnits().size(); i++ ) {
                            AbstractUnit t = unitsController.getPlayerUnits().get(i);
                            if (t.position.dst(tmp) < 30) {
                                selectedUnits.add(t);
                            }
                        }
                    }
                    selectionStart.set(-1, -1);
                }
                return true;
            }
        };
    }

    public void createGUIAndPrepareGameInput() {
        stage = new Stage(ScreenManager.getOurInstance().getViewport(), ScreenManager.getOurInstance().getBatch());
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, prepareInput()));
        Skin skin = new Skin();
        skin.addRegions(Assets.getOurInstance().getTextureAtlas());
        BitmapFont font14 = Assets.getOurInstance().getAssetManager().get("fonts/font14.ttf");
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                skin.getDrawable("smButton"), null, null, font14);
        final TextButton menuBtn = new TextButton("Menu", textButtonStyle);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getOurInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
        final TextButton testBtn = new TextButton("Test", textButtonStyle);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Pressed testBtn");;
            }
        });
        Group menuGroup = new Group();
        menuBtn.setPosition(0, 0);
        testBtn.setPosition(130, 0);
        menuGroup.addActor(menuBtn);
        menuGroup.addActor(testBtn);
        menuGroup.setPosition(900, 680);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font14, Color.WHITE);
        skin.add("simpleLabel", labelStyle);
        stage.addActor(menuGroup);
        guiPlayerInfo = new GuiPlayerInfo(playerLogic, skin);
        guiPlayerInfo.setPosition(0, 700);
        stage.addActor(guiPlayerInfo);
        skin.dispose();
    }
}
