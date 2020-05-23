package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameController {
    private BattleMap map;
    private Tank tank;

    public BattleMap getBattleMap() {
        return map;
    }

    public Tank getTank() {
        return tank;
    }

    public GameController() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("game.pack"));
        this.map = new BattleMap(atlas);
        this.tank = new Tank(200, 200, atlas);
    }

    public void update(float dt) {
        tank.update(dt);
    }
}
