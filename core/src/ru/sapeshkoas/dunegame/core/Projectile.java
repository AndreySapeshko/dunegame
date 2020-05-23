package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class Projectile {
    protected Vector2 position;
    protected Vector2 velocity;
    private boolean isActive;
    protected TextureRegion texture;


    public Projectile(TextureAtlas atlas, String textureName) {
        position = new Vector2(1.0f, 0.0f);
        texture = atlas.findRegion(textureName);
        isActive = false;
        velocity = new Vector2(0, 0);
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public abstract void setup(Vector2 startPosition, float angel);

    public void update(float dt) {
        position.mulAdd(velocity, dt);
    }

    public abstract void render(SpriteBatch batch);
}
