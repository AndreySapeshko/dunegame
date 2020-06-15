package ru.sapeshkoas.dunegame.core.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.sapeshkoas.dunegame.core.GameController;
import ru.sapeshkoas.dunegame.core.units.BattleTank;
import ru.sapeshkoas.dunegame.core.users_logic.BaseLogic;
import ru.sapeshkoas.dunegame.core.utils.ObjectPool;

public class BattleTanksController extends ObjectPool<BattleTank> {
    private GameController gc;

    @Override
    protected BattleTank newObject() {
        return new BattleTank(gc);
    }

    public BattleTanksController(GameController gc) {
        this.gc = gc;
    }

    public void setup(float x, float y, BaseLogic baseLogic) {
        BattleTank t = activateObject();
        t.setup(x, y, baseLogic);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
