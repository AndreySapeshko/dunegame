package ru.sapeshkoas.dunegame.core.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.GameController;
import ru.sapeshkoas.dunegame.core.Projectile;
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
                float dst = unit1.getPosition().dst(unit2.getPosition());
                if (dst < 60) {
                    float recoil = (60 - dst) / 2;
                    tmp.set(unit2.getPosition()).sub(unit1.getPosition()).nor().scl(recoil);
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
                if (p.getOwner().getBaseLogic() != unit.getBaseLogic() && unit.getPosition().dst(p.getPosition()) < 30) {
                    p.setActive(false);
                    for (int f = 0; f < 25; f++) {
                        tmp.set(p.getVelocity()).nor().scl(120.0f).add(MathUtils.random(-40, 40), MathUtils.random(-40, 40));
                        gc.getParticleController().setup(p.getPosition().x, p.getPosition().y, tmp.x, tmp.y, 0.5f, 1.0f, 0.4f,
                                1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);
                    }
                    unit.takeDamage(10);
                }
            }
        }
    }
}
