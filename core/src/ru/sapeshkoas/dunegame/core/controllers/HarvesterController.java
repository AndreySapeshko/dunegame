package ru.sapeshkoas.dunegame.core.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.sapeshkoas.dunegame.core.GameController;
import ru.sapeshkoas.dunegame.core.units.Harvester;
import ru.sapeshkoas.dunegame.core.users_logic.BaseLogic;
import ru.sapeshkoas.dunegame.core.utils.ObjectPool;

public class HarvesterController extends ObjectPool<Harvester> {
    private GameController gc;

    public HarvesterController(GameController gc) {
        this.gc = gc;
    }

    public void setup(float x, float y, BaseLogic baseLogic) {
        Harvester h = activateObject();
        h.setup(x, y, baseLogic);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    @Override
    protected Harvester newObject() {
        return new Harvester(gc);
    }
}
