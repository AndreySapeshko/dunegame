package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    protected Vector2 position;
    protected Vector2 velocity;



    public void setup(Vector2 startPosition, float angle) {
        velocity.set(100.0f * MathUtils.cosDeg(angle), 0.0f);
    };

    public void update(float dt) {
        position.mulAdd(velocity, dt);
    }
}
