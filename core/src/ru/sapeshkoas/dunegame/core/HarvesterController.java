package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.sapeshkoas.dunegame.core.units.Harvester;
import ru.sapeshkoas.dunegame.core.units.Owner;

public class HarvesterController extends ObjectPool<Harvester> {
    private GameController gc;

    public HarvesterController(GameController gc) {
        this.gc = gc;
    }

    public void setup(float x, float y, Owner owner) {
        Harvester h = getActiveElement();
        h.setup(x, y, owner);
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
