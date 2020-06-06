package ru.sapeshkoas.dunegame.core.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.*;

public class BattleTank extends AbstractUnit {

    public BattleTank(GameController gc) {
        super(gc);
        this.unitType = UnitType.BATTLE_TANK;
        this.textures = Assets.getOurInstance().getTextureAtlas().findRegion("tankcore").split(64, 64)[0];
        this.weaponTextures = Assets.getOurInstance().getTextureAtlas().findRegion("turret");
        this.weapon = new Weapon(1, 1);
        this.speed = 120;
        this.hpMax = 100;
        this.minDstToActiveTarget = 240;
        this.containerCapacity = 50;
    }

    @Override
    public void setup(float x, float y, Owner owner) {
        this.position.set(x, y);
        this.owner = owner;
        this.hp = hpMax;
        this.destination = new Vector2(position);
    }

    @Override
    public void commandAttack(Targetable target) {
        if (target.getTargetType() == TargetType.UNIT && ((AbstractUnit) target).getOwner() != this.owner) {
            this.target = target;
        } else {
            commandMoveTo(target.getPosition());
        }
    }

    @Override
    public void updateWeapon(float dt) {
        if (target != null) {
            if (!((AbstractUnit) target).isActive()) {
                target = null;
                return;
            }
            float angleTo = tmp.set(target.getPosition()).sub(position).angle();
            weapon.setWeaponAngle(rotateTo(weapon.getWeaponAngle(), angleTo, 180.0f, dt));
            int power = weapon.use(dt);
            if (power > - 1) {
                gc.getProjectileController().setup(this, position, angleTo);
            }
        }
        if (target == null) {
            weapon.setWeaponAngle(rotateTo(weapon.getWeaponAngle(), angle, 180.0f, dt));
        }
    }

    @Override
    public void renderGUI(SpriteBatch batch) {
        super.renderGUI(batch);
    }
}