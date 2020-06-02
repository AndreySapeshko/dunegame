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
    private TextureRegion progressbarTexture;
    private float angle;
    private float speed;
    private float rotationSpeed;
    private Weapon weapon;
    private int container;
    private int hp;
    private boolean operates;
    private BitmapFont font12;

    private float moveTimer;
    private float timePerFrame;

    public Tank(GameController gc) {
        super(gc);
        timePerFrame = 0.1f;
        rotationSpeed = 90.0f;
        progressbarTexture = Assets.getOurInstance().getTextureAtlas().findRegion("progressbar");

    }

    public void setup(float x, float y, Owner owner) {
        position.set(x, y);
        destination = new Vector2(position);
        textures = Assets.getOurInstance().getTextureAtlas().findRegion("tankanim").split(64, 64)[0];
        speed = 150.0f;
        weapon = new Weapon(Weapon.Type.HARVEST, 3.0f, 1);
        hp = 100;
        this.owner = owner;
        operates = false;
        font12 = Assets.getOurInstance().getAssetManager().get("fonts/font12.ttf");
    }

    public float getAngel() {
        return angle;
    }

    public Vector2 getPosition() {
        return position;
    }

    private int getCurrentFrameIndex() {
        return (int) (moveTimer / timePerFrame) % textures.length;
    }

    public void update(float dt) {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            tmp.set(Gdx.input.getX(), 720 - Gdx.input.getY());
            if (position.dst(tmp) < 30) {
                operates = true;
            } else {
                operates = false;
            }
        }

        if (operates) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                destination.set(Gdx.input.getX(), 720 - Gdx.input.getY());
            }
        }
        if (position.dst(destination) > 3.0f) {
            float angleTo = tmp.set(destination).sub(position).angle();
            float k = (Math.abs(angleTo - angle) < 180) ? 1 : -1;
            if (Math.abs(angleTo - angle) > 3.0f) {
                if (angleTo - angle > 0) {
                    angle += rotationSpeed * dt * k;
                } else {
                    angle -= rotationSpeed * dt * k;
                }
            }
            if (angle < 0.0f) {
                angle += 360.0f;
            }
            if (angle > 360.0f) {
                angle -= 360.0f;
            }
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

    public void updateWeapon(float dt) {
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
        tmp.set(position);
        tmp.add(30 * MathUtils.cosDeg(angle), 30 * MathUtils.sinDeg(angle));
        projectileController.setup(tmp, angle);
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
        batch.draw(textures[getCurrentFrameIndex()],position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1,angle);
        if (weapon.getType() == Weapon.Type.HARVEST && weapon.getUsageTimePercentage() > 0 && container < 50) {
            batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 32, position.y + 30, 54, 12);
            batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 30, position.y + 32, 50 * weapon.getUsageTimePercentage(), 8);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        }
        font12.draw(batch, "" + container, position.x + 24, position.y + 42, 12, 1, false);
    }

    @Override
    public boolean isActive() {
        return hp > 0;
    }
}
