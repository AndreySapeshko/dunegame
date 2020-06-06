package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.units.AbstractUnit;
import ru.sapeshkoas.dunegame.core.units.Owner;

import java.util.ArrayList;
import java.util.List;

public class UnitsController {
    private GameController gc;
    private BattleTanksController tanksController;
    private HarvesterController harvesterController;
    private List<AbstractUnit> units;
    private List<AbstractUnit> playerUnits;
    private List<AbstractUnit> aiUnits;

    public List<AbstractUnit> getUnits() {
        return units;
    }

    public List<AbstractUnit> getPlayerUnits() {
        return playerUnits;
    }

    public List<AbstractUnit> getAiUnits() {
        return aiUnits;
    }

    public UnitsController(GameController gc) {
        this.gc = gc;
        this.tanksController = new BattleTanksController(gc);
        this.harvesterController = new HarvesterController(gc);
        units = new ArrayList<>();
        playerUnits = new ArrayList<>();
        aiUnits = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tanksController.setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Owner.PLAYER);
        }
        for (int i = 0; i < 2; i++) {
            harvesterController.setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Owner.PLAYER);
        }
        for (int i = 0; i < 2; i++) {
            tanksController.setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Owner.AI);
        }
        for (int i = 0; i < 2; i++) {
            harvesterController.setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Owner.AI);
        }
    }

    public void update(float dt) {
        tanksController.update(dt);
        harvesterController.update(dt);
        units.clear();
        playerUnits.clear();
        aiUnits.clear();
        units.addAll(tanksController.getActiveList());
        units.addAll(harvesterController.getActiveList());
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getOwner() == Owner.AI) {
                aiUnits.add(units.get(i));
            }
            if (units.get(i).getOwner() == Owner.PLAYER) {
                playerUnits.add(units.get(i));
            }

        }
    }

    public void render(SpriteBatch batch) {
        tanksController.render(batch);
        harvesterController.render(batch);
    }

    public AbstractUnit getNearestAiUnit(Vector2 point) {
        for (int i = 0; i < aiUnits.size(); i++) {
            AbstractUnit u = aiUnits.get(i);
            if (u.getPosition().dst(point) < 30) {
                return u;
            }
        }
        return null;
    }
}
