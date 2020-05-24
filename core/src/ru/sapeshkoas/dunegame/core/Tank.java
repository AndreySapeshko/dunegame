package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Tank {
    private Vector2 position;
    private Vector2 tmp;
    private TextureRegion[] textures;
    private float angle;
    private float speed;
    private Projectile projectile;

    private float moveTimer;
    private float timePerFrame;

    public Tank(float x, float y, TextureAtlas atlas) {
        position = new Vector2(x, y);
        tmp = new Vector2(1.0f, 0.0f);
        textures = new TextureRegion(atlas.findRegion("tankanim")).split(64, 64)[0];
        projectile = new Projectile(atlas);
        speed = 150.0f;
        timePerFrame = 0.1f;
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
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angle += 180.f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            angle -= 180.f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.add(speed * MathUtils.cosDeg(angle) * dt, speed * MathUtils.sinDeg(angle) * dt);

            moveTimer += dt;
        } else {
            if (getCurrentFrameIndex() != 0) {
                moveTimer += dt;
            }
        }
        checkBounds();
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            if (!projectile.isActive()) {
                shot(projectile);
            }
        }
        if (projectile.isActive()) {
            projectile.update(dt);
        }
    }

    public void shot(Projectile projectile) {
        tmp.set(position);
        tmp.add(30 * MathUtils.cosDeg(angle), 30 * MathUtils.sinDeg(angle));
        projectile.setup(tmp, angle);
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
        projectile.render(batch);
    }
}
