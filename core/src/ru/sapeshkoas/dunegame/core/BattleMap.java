package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class BattleMap {
    private class Cell {
        private int cellX, cellY;
        private int resource;
        private float resourceRegenerationRate;
        private float resourceRegenerationTime;

        public Cell(int x, int y) {
            this.cellX = x;
            this.cellY = y;
            if (MathUtils.random() < 0.1f) {
                resource = MathUtils.random(1, 3);
            }
            resourceRegenerationRate = MathUtils.random(5.0f) - 4.5f;
            if (resourceRegenerationRate > 0) {
                resourceRegenerationRate *= 20.0f;
                resourceRegenerationRate += 10.0f;
            } else {
                resourceRegenerationRate = 0.0f;
            }
        }

        public void update(float dt) {
            if (resourceRegenerationRate > 0.01f) {
                resourceRegenerationTime += dt;
                if (resourceRegenerationTime > resourceRegenerationRate) {
                    resourceRegenerationTime = 0.0f;
                    resource++;
                    if (resource > 5) {
                        resource = 5;
                    }
                }
            }
        }

        public void render(SpriteBatch batch) {
            if (resource > 0) {
                float scale = 0.5f + resource * 0.2f;
                batch.draw(resourceTexture, cellX * 80, cellY * 80, 40, 40, 80, 80, scale, scale, 0.0f);
            } else {
                if (resourceRegenerationRate > 0.01f) {
                    batch.draw(resourceTexture, cellX * 80, cellY * 80, 40, 40, 80, 80, 0.1f, 0.1f, 0.0f);
                }
            }
        }
    }
    public static final int COLUMN_COUNT = 16;
    public static final int ROWS_COUNT = 9;
    public static final int CELL_SIZE = 80;

    private Cell[][] cells;
    private TextureRegion grassTexture;
    private TextureRegion resourceTexture;

    public BattleMap() {
        grassTexture = Assets.getOurInstance().getTextureAtlas().findRegion("grass");
        resourceTexture = Assets.getOurInstance().getTextureAtlas().findRegion("trophy");
        cells = new Cell[COLUMN_COUNT][ROWS_COUNT];
        for (int x = 0; x < COLUMN_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    public int getResourceCount(Tank harvester) {
        return cells[harvester.getCellX()][harvester.getCellY()].resource;
    }

    public int harvesterResource(Tank harvester, int power) {
        int value = 0;
        if (cells[harvester.getCellX()][harvester.getCellY()].resource >= power) {
            value = power;
            cells[harvester.getCellX()][harvester.getCellY()].resource -= power;
        } else {
            value = cells[harvester.getCellX()][harvester.getCellY()].resource;
            cells[harvester.getCellX()][harvester.getCellY()].resource = 0;
        }
        return value;
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 9; y++) {
                batch.draw(grassTexture, x * 80, y * 80);
                cells[x][y].render(batch);
            }
        }
    }

    public void update(float dt) {
        for (int x = 0; x < COLUMN_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                cells[x][y].update(dt);
            }
        }
    }
}
