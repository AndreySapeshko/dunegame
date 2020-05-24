package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    protected Vector2 position;
    protected Vector2 velocity;
    private boolean isActive;
    protected TextureRegion texture;


    public Projectile(TextureAtlas atlas) {
        position = new Vector2(0.0f, 0.0f);
        texture = atlas.findRegion("bullet");
        isActive = false;
        velocity = new Vector2(0, 0);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setup(Vector2 startPosition, float angle){
        position.set(startPosition);
        velocity.set(600.0f * MathUtils.cosDeg(angle), 600.0f * MathUtils.sinDeg(angle));
        setActive(true);
    }

    public void update(float dt) {
        if (position.x < 0 || position.x > 1280 || position.y < 0 || position.y > 720) {
            setActive(false);
        } else {
            position.mulAdd(velocity, dt);
        }
    }

    public void render(SpriteBatch batch) {
        if (isActive) {
            batch.draw(texture, position.x - 8, position.y - 8);
        }

    }
}
