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


    public BattleMap() {
        grassTexture = Assets.getOurInstance().getTextureAtlas().findRegion("grass");
        trophyTexture = Assets.getOurInstance().getTextureAtlas().findRegion("trophy");

    }



    public void render(SpriteBatch batch) {
        for (int x = 0; x < 16; x++) {
            int trophyY = (int) (Math.random() * 10);
            for (int y = 0; y < 9; y++) {
                batch.draw(grassTexture, x * 80, y * 80);
            }
        }

    }
}
