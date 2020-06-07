package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.units.AbstractUnit;
import ru.sapeshkoas.dunegame.core.units.BattleTank;
import ru.sapeshkoas.dunegame.core.units.Owner;
import ru.sapeshkoas.dunegame.screens.ScreenManager;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private BattleMap map;
    private PlayerLogic playerLogic;
    private AiLogic aiLogic;
    private UnitsController unitsController;
    private ProjectileController projectileController;
    private Vector2 tmp;
    private Vector2 mouse;
    private Vector2 selectionStart;
    private List<AbstractUnit> selectedUnits;
    private Collider collider;

    public ProjectileController getProjectileController() {
        return projectileController;
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
        prepareInput();
    }

    public boolean isUnitSelected(AbstractUnit abstractUnint) {
        return selectedUnits.contains(abstractUnint);
    }

    public void update(float dt) {
        mouse.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getOurInstance().getViewport().unproject(mouse);
        unitsController.update(dt);
        playerLogic.update(dt);
        aiLogic.update(dt);
        map.update(dt);
        projectileController.update(dt);
        collider.checkCollision(dt);
    }

    public void prepareInput() {
        InputProcessor ip = new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    selectionStart.set(screenX, 720 - screenY);
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
                }
                return true;
            }
        };
        Gdx.input.setInputProcessor(ip);
    }
}
