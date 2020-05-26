package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class BattleMap {
    private TextureRegion grassTexture;
    private TextureRegion trophyTexture;
    private GameController gc;
    private List<Vector2> trophy;

    public BattleMap(GameController gc) {
        this.gc = gc;
        grassTexture = Assets.getOurInstance().getTextureAtlas().findRegion("grass");
        trophyTexture = Assets.getOurInstance().getTextureAtlas().findRegion("trophy");
        trophy = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            trophy.add(new Vector2((float) (Math.random() * 1280), (float) Math.random() * 720));
        }
    }

    public List<Vector2> getTrophy() {
        return trophy;
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < 16; x++) {
            int trophyY = (int) (Math.random() * 10);
            for (int y = 0; y < 9; y++) {
                batch.draw(grassTexture, x * 80, y * 80);
            }
        }
        for (int i = 0; i < trophy.size(); i++) {
            batch.draw(trophyTexture,trophy.get(i).x - 40, trophy.get(i).y - 40);
            if (trophy.get(i).dst(gc.getTank().position) < 50) {
                trophy.remove(i);
            }
        }
    }
}
