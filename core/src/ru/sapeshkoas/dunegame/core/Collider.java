package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.units.AbstractUnit;
import ru.sapeshkoas.dunegame.core.units.BattleTank;

import java.util.List;

public class Collider {
    private GameController gc;
    private Vector2 tmp;

    public Collider(GameController gc) {
        this.gc = gc;
        this.tmp = new Vector2();
    }

    public void checkCollision(float dt) {
        List<AbstractUnit> units = gc.getUnitsController().getUnits();
        for (int i = 0; i < units.size() - 1; i++) {
            AbstractUnit unit1 = units.get(i);
            for (int j = i + 1; j < units.size(); j++) {
                AbstractUnit unit2 = units.get(j);
                float dst = unit1.position.dst(unit2.position);
                if (dst < 60) {
                    float recoil = (60 - dst) / 2;
                    tmp.set(unit2.position).sub(unit1.position).nor().scl(recoil);
                    unit2.moveBy(tmp);
                    tmp.scl(-1);
                    unit1.moveBy(tmp);
                }
            }
        }
        for (int i = 0; i < gc.getProjectileController().getActiveList().size(); i++) {
            Projectile p = gc.getProjectileController().getActiveList().get(i);
            for (int j = 0; j < units.size(); j++) {
                AbstractUnit unit = units.get(j);
                if (p.getOwner() != unit && unit.getPosition().dst(p.getPosition()) < 30) {
                    p.setActive(false);
                    unit.takeDamage(10);
                }
            }
        }
    }
}
