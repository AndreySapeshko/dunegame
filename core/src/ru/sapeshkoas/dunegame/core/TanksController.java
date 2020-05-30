package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TanksController extends ObjectPool<Tank> {
    private GameController gc;

    @Override
    protected Tank newObject() {
        return new Tank(gc);
    }

    public TanksController(GameController gc) {
        this.gc = gc;
    }

    public void setup(float x, float y, Tank.Owner owner) {
        Tank t = getActiveElement();
        t.setup(x, y, owner);
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
