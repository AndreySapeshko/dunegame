package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BattleMap {
    private TextureRegion grassTexture;

    public BattleMap(TextureAtlas atlas) {
        grassTexture = atlas.findRegion("grass");
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 9; y++) {
                batch.draw(grassTexture, x * 80, y * 80);
            }
        }
    }
}
