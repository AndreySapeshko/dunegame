package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private BattleMap map;
    private TanksController tanksController;
    private ProjectileController projectileController;
    private Vector2 tmp;
    private Vector2 selectionStart;
    private List<Tank> selectedUnits;

    public ProjectileController getProjectileController() {
        return projectileController;
    }

    public BattleMap getBattleMap() {
        return map;
    }

    public TanksController getTanksController() {
        return tanksController;
    }

    public List<Tank> getSelectedUnits() {
        return selectedUnits;
    }

    public GameController() {
        Assets.getOurInstance().loadAssets();
        this.map = new BattleMap();
        this.tmp = new Vector2();
        this.selectionStart = new Vector2();
        this.selectedUnits = new ArrayList<>();
        this.tanksController = new TanksController(this);
        for (int i = 0; i < 5; i++) {
            this.tanksController.setup(MathUtils.random(40, 1240), MathUtils.random(40, 680), Tank.Owner.PLAYER);
        }
        for (int i = 0; i < 2; i++) {
            this.tanksController.setup(MathUtils.random(40, 1240), MathUtils.random(40, 680), Tank.Owner.AI);
        }
        this.projectileController = new ProjectileController();
    }

    public boolean isTankSelected(Tank tank) {
        return selectedUnits.contains(tank);
    }

    public void update(float dt) {
        tanksController.update(dt);
        map.update(dt);
        projectileController.update(dt);
        prepareInput();
        checkCollision(dt);
    }

    public void checkCollision(float dt) {
        for (int i = 0; i < tanksController.activeList.size(); i++) {
            Tank t1 = tanksController.activeList.get(i);
            for (int j = i + 1; j < tanksController.activeList.size(); j++) {
                Tank t2 = tanksController.activeList.get(j);
                float dst = t1.position.dst(t2.position);
                if (dst < 60) {
                    float recoil = (60 - dst) / 2;
                    tmp.set(t2.position).sub(t1.position).nor().scl(recoil);
                    t2.moveBy(tmp);
                    tmp.scl(-1);
                    t1.moveBy(tmp);
                }
            }
        }
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
                    tmp.set(Gdx.input.getX(), 720 - Gdx.input.getY());
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
                        for (int i = 0; i < tanksController.activeList.size(); i++ ) {
                            Tank t = tanksController.activeList.get(i);
                            if (t.getOwner() == Tank.Owner.PLAYER && t.position.x < tmp.x &&
                            t.position.y < selectionStart.y && t.position.y > tmp.y &&
                                    t.position.x > selectionStart.x) {
                                selectedUnits.add(t);
                            }
                        }
                    } else {
                        for (int i = 0; i < tanksController.activeList.size(); i++ ) {
                            Tank t = tanksController.activeList.get(i);
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
