package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.units.AbstractUnit;

public class Projectile extends GameObject implements Poolable {
    private AbstractUnit owner;
    private Vector2 velocity;
    private boolean isActive;
    private TextureRegion texture;
    private float angle;
    private float speed;


    public Projectile(GameController gc) {
        super(gc);
        velocity = new Vector2();
        speed = 640.0f;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public AbstractUnit getOwner() {
        return owner;
    }

    public void setup(AbstractUnit owner, Vector2 startPosition, float angle, TextureRegion texture){
        this.owner = owner;
        this.texture = texture;
        position.set(startPosition);
        this.angle = angle;
        velocity.set(speed * MathUtils.cosDeg(angle), speed * MathUtils.sinDeg(angle));
        isActive = true;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < 0 || position.x > 1280 || position.y < 0 || position.y > 720) {
            setActive(false);
        }
    }

    public void render(SpriteBatch batch) {
        if (isActive) {
            batch.draw(texture, position.x - 8, position.y - 8);
        }
    }
}
