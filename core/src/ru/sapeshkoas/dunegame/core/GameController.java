package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameController {
    private BattleMap map;
    private TanksController tanksController;
    private ProjectileController projectileController;

    public ProjectileController getProjectileController() {
        return projectileController;
    }

    public BattleMap getBattleMap() {
        return map;
    }

    public TanksController getTanksController() {
        return tanksController;
    }

    public GameController() {
        Assets.getOurInstance().loadAssets();
        this.map = new BattleMap();
        this.tanksController = new TanksController(this);
        this.tanksController.setup(200, 200, Tank.Owner.PLAYER);
        this.tanksController.setup(200, 300, Tank.Owner.PLAYER);
        this.projectileController = new ProjectileController();
    }

    public void update(float dt) {
        tanksController.update(dt);
        map.update(dt);
        projectileController.update(dt);
        checkCollision(dt);
    }

    public void checkCollision(float dt) {
        for (int i = 0; i < tanksController.activeList.size(); i++) {
            for (int j = i + 1; j < tanksController.activeList.size(); j++) {
                if (tanksController.activeList.get(i).position.dst(tanksController.activeList.get(j).position) < 60) {
                    float angle = tanksController.activeList.get(i).getAngel();
                    Vector2 tmp = tanksController.activeList.get(i).tmp.set(3.0f, 0.0f);
                    if (angle > 180) {
                        tmp.rotate(angle + 180);
                    } else {
                        tmp.rotate(angle - 180);
                    }
                    tanksController.activeList.get(i).position.add(tmp);
                }
            }
        }
    }
}
