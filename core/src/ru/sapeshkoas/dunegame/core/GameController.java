package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

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
        this.projectileController = new ProjectileController();
    }

    public void update(float dt) {
        tanksController.update(dt);
        map.update(dt);
        projectileController.update(dt);
    }
}
