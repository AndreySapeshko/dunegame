package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ProjectileController extends ObjectPool<Projectile> {
    private GameController gc;
    private TextureRegion texture;

    public ProjectileController() {
        super();
        texture = Assets.getOurInstance().getTextureAtlas().findRegion("bullet");
    }

    @Override
    protected Projectile newObject() {
        return new Projectile(gc);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void setup(Vector2 srcPosition, float angle) {
        Projectile p = getActiveElement();
        p.setup(srcPosition, angle, texture);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
