package ru.sapeshkoas.dunegame.core.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.*;
import ru.sapeshkoas.dunegame.core.interfaces.Poolable;
import ru.sapeshkoas.dunegame.core.interfaces.Targetable;
import ru.sapeshkoas.dunegame.core.units.types.UnitType;
import ru.sapeshkoas.dunegame.core.users_logic.BaseLogic;
import ru.sapeshkoas.dunegame.screens.utils.Assets;

public class Harvester extends AbstractUnit implements Poolable, Targetable {
    public Harvester(GameController gc) {
        super(gc);
        this.unitType = UnitType.HARVESTER;
        this.textures = Assets.getOurInstance().getTextureAtlas().findRegion("tankcore").split(64, 64)[0];
        this.weaponTextures = Assets.getOurInstance().getTextureAtlas().findRegion("harvester");
        this.weapon = new Weapon(4.0f, 1);
        this.speed = 120;
        this.hpMax = 300;
        this.minDstToActiveTarget = 5.0f;
        this.containerCapacity = 10;
    }

    @Override
    public void setup(float x, float y, BaseLogic baseLogic) {
        this.position.set(x, y);
        this.baseLogic = baseLogic;
        this.owner = this.baseLogic.getOwner();
        this.hp = hpMax;
        this.destination = new Vector2(position);
    }

    @Override
    public void commandAttack(Targetable target) {
        commandMoveTo(target.getPosition());
    }

    @Override
    public void updateWeapon(float dt) {
        weapon.setWeaponAngle(angle);
        if (gc.getBattleMap().getResourceCount(position) > 0) {
            int result = weapon.use(dt);
            if (result > -1) {
                container += gc.getBattleMap().harvesterResource(position, result);
                if (container > containerCapacity) {
                    container = containerCapacity;
                }
            }
        } else {
            weapon.reset();
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        Building b = gc.getBattleMap().getBuildingEntrance(getCellX(), getCellY());
        if (b != null && b.getType() == Building.Type.STOCK && b.getOwnerLogic() == baseLogic) {
            baseLogic.addMoney(container * 100);
            container = 0;
        }
    }

    @Override
    public void renderGUI(SpriteBatch batch) {
        super.renderGUI(batch);
        if (weapon.getUsageTimePercentage() > 0.0f) {
            batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 32, position.y + 22, 64, 8);
            batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 30, position.y + 24, 60 * weapon.getUsageTimePercentage(), 4);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
