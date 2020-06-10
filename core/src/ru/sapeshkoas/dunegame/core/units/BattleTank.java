package ru.sapeshkoas.dunegame.core.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
        if (owner == Owner.AI) {
            warehouseX = BattleMap.AI_WAREHOUSE_X;
            warehouseY = BattleMap.AI_WAREHOUSE_Y;
        }
        if (owner == Owner.PLAYER) {
            warehouseX = BattleMap.PLAYER_WAREHOUSE_X;
            warehouseY = BattleMap.PLAYER_WAREHOUSE_Y;
        }
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
                tmp.nor().scl(30).add(position);
                float particleX = tmp.x;
                float particleY = tmp.y;
                gc.getProjectileController().setup(this, tmp, angleTo);
                for (int i = 0; i < 10; i++) {
                    tmp.set(1.0f, 0.0f).rotate(angleTo).scl(550.0f - 20 * i);
                    gc.getParticleController().setup(particleX + MathUtils.random(-1, 1), particleY + MathUtils.random(-1, 1),
                            tmp.x, tmp.y, 0.4f, 1.0f, 0.3f,
                            1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
        if (target == null) {
            weapon.setWeaponAngle(rotateTo(weapon.getWeaponAngle(), angle, 180.0f, dt));
        }
    }

    @Override
    public void unloadingAndRepair(float dt) {
        int result = weapon.use(dt);
        if (result > - 1) {
            if (owner == Owner.AI) {
                gc.getAiLogic().setMoney(hp - hpMax);
                hp = hpMax;
            }
            if (owner == Owner.PLAYER) {
                gc.getPlayerLogic().setMoney(hp - hpMax);
                hp = hpMax;
            }
        }
    }

    @Override
    public void renderGUI(SpriteBatch batch) {
        super.renderGUI(batch);
    }
}
