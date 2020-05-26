package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends GameObject implements Poolable {
    private Vector2 velocity;
    private boolean isActive;
    private TextureRegion texture;
    private float angle;
    private float speed;


    public Projectile(GameController gc) {
        super(gc);
        isActive = false;
        velocity = new Vector2(0, 0);
        speed = 350.0f;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setup(Vector2 startPosition, float angle, TextureRegion texture){
        this.texture = texture;
        position.set(startPosition);
        this.angle = angle;
        velocity.set(speed * MathUtils.cosDeg(angle), speed * MathUtils.sinDeg(angle));
        setActive(true);
    }

    public void update(float dt) {
        if (isActive) {
            if (position.x < 0 || position.x > 1280 || position.y < 0 || position.y > 720) {
                setActive(false);
            } else {
                position.mulAdd(velocity, dt);
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (isActive) {
            batch.draw(texture, position.x - 8, position.y - 8);
        }

    }
}
