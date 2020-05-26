package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameController {
    private BattleMap map;
    private Tank tank;
    private ProjectileController projectileController;

    public ProjectileController getProjectileController() {
        return projectileController;
    }

    public BattleMap getBattleMap() {
        return map;
    }

    public Tank getTank() {
        return tank;
    }

    public GameController() {
        Assets.getOurInstance().loadAssets();
        this.map = new BattleMap();
        this.tank = new Tank(200, 200, this);
        this.projectileController = new ProjectileController();
    }

    public void update(float dt) {
        tank.update(dt);
        projectileController.update(dt);
    }
}
