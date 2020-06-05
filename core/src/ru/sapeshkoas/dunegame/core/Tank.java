package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Tank extends GameObject implements Poolable {
    public enum Owner {
        PLAYER, AI
    }

    private Owner owner;
    private Vector2 destination;
    private TextureRegion[] textures;
    private TextureRegion[] weaponTextures;
    private TextureRegion progressbarTexture;
    private float angle;
    private float speed;
    private float rotationSpeed;
    private Weapon weapon;
    private int container;
    private int hp;
    private boolean operates;
    private BitmapFont font12;
    private Tank target;

    private float moveTimer;
    private float timePerFrame;
    private float lifeTime;

    public Tank(GameController gc) {
        super(gc);
        timePerFrame = 0.1f;
        rotationSpeed = 90.0f;
        progressbarTexture = Assets.getOurInstance().getTextureAtlas().findRegion("progressbar");
        weaponTextures = new TextureRegion[] {
                Assets.getOurInstance().getTextureAtlas().findRegion("turret"),
                Assets.getOurInstance().getTextureAtlas().findRegion("harvester")
        };

    }

    public void setup(float x, float y, Owner owner) {
        position.set(x, y);
        destination = new Vector2(position);
        textures = Assets.getOurInstance().getTextureAtlas().findRegion("tankcore").split(64, 64)[0];
        speed = 150.0f;
        if (MathUtils.random() < 0.5) {
            weapon = new Weapon(Weapon.Type.HARVEST, 3.0f, 1);
        } else {
            weapon = new Weapon(Weapon.Type.GROUND, 1.0f, 1);
        }
        hp = 100;
        this.owner = owner;
        operates = false;
        font12 = Assets.getOurInstance().getAssetManager().get("fonts/font12.ttf");
    }



    public Weapon getWeapon() {
        return weapon;
    }

    public Owner getOwner() {
        return owner;
    }

    public float getAngel() {
        return angle;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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
            destination.set(target.position);
            if (destination.dst(position) < 240.0f) {
                destination.set(position);
            }
            if (target.getHp() <= 0) {
                target = null;
            }
        }
        if (position.dst(destination) > 3.0f) {
            float angleTo = tmp.set(destination).sub(position).angle();
            angle = rotateTo(angle, angleTo, rotationSpeed, dt);
            moveTimer += dt;
            tmp.set(speed, 0).rotate(angle);
            if ((position.dst(destination) > 100 && Math.abs(angleTo - angle) < 40) || Math.abs(angleTo - angle) < 10 ||
                    position.dst(destination) > 200) {
                position.mulAdd(tmp, dt);
            }
        }
        checkBounds();
        updateWeapon(dt);
//        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
//            shot(gc.getProjectileController());
//        }

    }

    public void commandMoveTo(Vector2 point) {
        destination.set(point);
    }

    public void commandAttack(Tank target) {
        this.target = target;
    }

    public void updateWeapon(float dt) {
        if (target == null) {
            weapon.setWeaponAngle(angle);
        }
        if (weapon.getType() == Weapon.Type.GROUND && target != null) {
            float angleTo = tmp.set(target.position).sub(position).angle();
            weapon.setWeaponAngle(rotateTo(weapon.getWeaponAngle(), angleTo, 180.0f, dt));
            int power = weapon.use(dt);
            if (power > - 1) {
                gc.getProjectileController().setup(position, target, weapon.getWeaponAngle());
            }
        }
        if (weapon.getType() == Weapon.Type.HARVEST) {
            if (gc.getBattleMap().getResourceCount(this) > 0) {
                int result = weapon.use(dt);
                if (result > - 1) {
                    if (container < 50) {
                        container += gc.getBattleMap().harvesterResource(this, result);
                    }
                }
            } else {
                weapon.reset();
            }
        }
    }

    public void shot(ProjectileController projectileController) {
//        tmp.set(position);
//        tmp.add(30 * MathUtils.cosDeg(angle), 30 * MathUtils.sinDeg(angle));
//        projectileController.setup(tmp, angle);
    }

    public void checkBounds() {
        if (position.x < 40) {
            position.x = 40;
        }
        if (position.x > 1240) {
            position.x = 1240;
        }
        if (position.y < 40) {
            position.y = 40;
        }
        if (position.y > 680) {
            position.y = 680;
        }
    }

    public void render(SpriteBatch batch) {
        if (gc.isTankSelected(this)) {
            float c = 0.7f + (float) Math.sin(lifeTime * 8.0f) * 0.3f;
            batch.setColor(c, c, c, 1.0f);
        }
        batch.draw(textures[getCurrentFrameIndex()],position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1,angle);
        batch.draw(weaponTextures[getWeapon().getType().getImageIndex()], position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, getWeapon().getWeaponAngle());
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (weapon.getType() == Weapon.Type.HARVEST && weapon.getUsageTimePercentage() > 0 && container < 50) {
            batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 32, position.y + 30, 54, 12);
            batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 30, position.y + 32, 50 * weapon.getUsageTimePercentage(), 8);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        }
        font12.draw(batch, "" + container, position.x + 24, position.y + 42, 12, 1, false);
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
