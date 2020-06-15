package ru.sapeshkoas.dunegame.core.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.sapeshkoas.dunegame.core.Building;
import ru.sapeshkoas.dunegame.core.GameController;
import ru.sapeshkoas.dunegame.core.users_logic.BaseLogic;
import ru.sapeshkoas.dunegame.core.utils.ObjectPool;

public class BuildingController extends ObjectPool<Building> {
    private GameController gc;

    public BuildingController(GameController gc) {
        this.gc = gc;
    }

    @Override
    protected Building newObject() {
        return new Building(gc);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void setup(int cellX, int cellY, BaseLogic ownerLogic) {
        Building b = activateObject();
        b.setup(cellX, cellY, ownerLogic);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
