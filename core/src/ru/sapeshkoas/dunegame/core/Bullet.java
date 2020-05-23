package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Projectile {

    public Bullet(TextureAtlas atlas, String textureName) {
        super(atlas, textureName);
    }

    @Override
    public void setup(Vector2 startPosition, float angel) {
        velocity.set(600.0f * MathUtils.cosDeg(angel), 600.0f * MathUtils.sinDeg(angel));
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 8, position.y - 8, 8, 8, 16, 16, 1, 1, 0.0f);
    }
}
