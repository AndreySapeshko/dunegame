package ru.sapeshkoas.dunegame.core.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.*;
import ru.sapeshkoas.dunegame.core.interfaces.Poolable;
import ru.sapeshkoas.dunegame.core.interfaces.Targetable;
import ru.sapeshkoas.dunegame.core.units.types.Owner;
import ru.sapeshkoas.dunegame.core.units.types.TargetType;
import ru.sapeshkoas.dunegame.core.units.types.UnitType;
import ru.sapeshkoas.dunegame.core.users_logic.BaseLogic;
import ru.sapeshkoas.dunegame.screens.utils.Assets;

public abstract class AbstractUnit extends GameObject implements Poolable, Targetable {

    protected UnitType unitType;
    protected BaseLogic baseLogic;
    protected Owner owner;
    protected Weapon weapon;
    protected Vector2 destination;
    protected TextureRegion[] textures;
    protected TextureRegion weaponTextures;
    protected TextureRegion progressbarTexture;
    protected float angle;
    protected float speed;
    protected float rotationSpeed;
    protected int container;
    protected int containerCapacity;
    protected int hp;
    protected int hpMax;
    protected Targetable target;
    protected float minDstToActiveTarget;

    protected float moveTimer;
    protected float timePerFrame;
    protected float lifeTime;

    @Override
    public TargetType getTargetType() {
        return TargetType.UNIT;
    }

    public BaseLogic getBaseLogic() {
        return baseLogic;
    }

    public Targetable getTarget() {
        return target;
    }

    public boolean takeDamage(int damage) {
        if (!isActive()) {
            return false;
        }
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            return true;
        }
        return false;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public AbstractUnit(GameController gc) {
        super(gc);
        timePerFrame = 0.1f;
        rotationSpeed = 90.0f;
        progressbarTexture = Assets.getOurInstance().getTextureAtlas().findRegion("progressbar");
    }

    public abstract void setup(float x, float y, BaseLogic baseLogic);

    public Owner getOwner() {
        return owner;
    }

    public float getAngle() {
        return angle;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDestination() {
        return destination;
    }

    public int getContainer() {
        return container;
    }

    public int getContainerCapacity() {
        return containerCapacity;
    }

    public int getHp() {
        return hp;
    }

    private int getCurrentFrameIndex() {
        return (int) (moveTimer / timePerFrame) % textures.length;
    }

    @Override
    public void moveBy(Vector2 toPoint) {
        boolean stayStill = false;
        if (position.dst(toPoint) < 3.0f) {
            stayStill = true;
        }
        position.add(toPoint);
        if (stayStill) {
            destination.set(position);
        }
    }

    public void update(float dt) {
        lifeTime += dt;
        if (target != null) {
            destination.set(target.getPosition());
            if (destination.dst(position) < minDstToActiveTarget) {
                destination.set(position);
            }
        }
        if (position.dst(destination) > 3.0f) {
            float angleTo = tmp.set(destination).sub(position).angle();
            angle = rotateTo(angle, angleTo, rotationSpeed, dt);
            moveTimer += dt;
            tmp.set(speed, 0).rotate(angle);
            position.mulAdd(tmp, dt);
            if ((position.dst(destination) < 120 && Math.abs(angleTo - angle) > 10) || !gc.getBattleMap().isCellGroundPassable(position)) {
                position.mulAdd(tmp, -dt);
            }
        }
        updateWeapon(dt);
        checkBounds();
    }

    public void commandMoveTo(Vector2 point) {
        destination.set(point);
        target = null;
    }

    public abstract void commandAttack(Targetable target);

    public abstract void updateWeapon(float dt);

    public void checkBounds() {
        if (position.x < 32) {
            position.x = 32;
        }
        if (position.x > BattleMap.MAP_WIDTH_PX - 32) {
            position.x = BattleMap.MAP_WIDTH_PX - 32;
        }
        if (position.y < 32) {
            position.y = 32;
        }
        if (position.y > BattleMap.MAP_HEIGHT_PX - 32) {
            position.y = BattleMap.MAP_HEIGHT_PX - 32;
        }
    }

    public void render(SpriteBatch batch) {
        float c = 1.0f;
        float r = 0.0f;
        if (gc.isUnitSelected(this)) {
            c = 0.7f + (float) Math.sin(lifeTime * 8.0f) * 0.3f;
        }
        if (owner == Owner.AI) {
            r = 0.4f;
        }
        batch.setColor(c, c - r, c - r, 1.0f);
        batch.draw(textures[getCurrentFrameIndex()],position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1,angle);
        batch.draw(weaponTextures, position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, weapon.getWeaponAngle());
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        renderGUI(batch);
    }

    public void renderGUI(SpriteBatch batch) {
        if (hp < hpMax) {
            batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 32, position.y + 30, 64, 12);
            batch.setColor(0.0f, 1.0f, 0.0f, 1.0f);
            float percentage = (float) hp / hpMax;
            batch.draw(progressbarTexture, position.x - 30, position.y + 32, 60 * percentage, 8);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public float rotateTo(float srcAngle, float angleTo, float rSpeed, float dt) {
        float k = (Math.abs(angleTo - srcAngle) < 180) ? 1 : -1;
        if (Math.abs(angleTo - srcAngle) > 3.0f) {
            if (angleTo - srcAngle > 0) {
                srcAngle += rSpeed * dt * k;
            } else {
                srcAngle -= rSpeed * dt * k;
            }
        }
        if (srcAngle < 0.0f) {
            srcAngle += 360.0f;
        }
        if (srcAngle > 360.0f) {
            srcAngle -= 360.0f;
        }
        return srcAngle;
    }

    @Override
    public boolean isActive() {
        return hp > 0;
    }
}
