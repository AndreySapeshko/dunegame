package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.units.BattleTank;
import ru.sapeshkoas.dunegame.core.units.Harvester;

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
    public static final int COLUMN_COUNT = 20;
    public static final int ROWS_COUNT = 12;
    public static final int CELL_SIZE = 80;
    public static final int MAP_WIDTH_PX = COLUMN_COUNT * CELL_SIZE;
    public static final int MAP_HEIGHT_PX = ROWS_COUNT * CELL_SIZE;
    public static final int AI_WAREHOUSE_X = 40;
    public static final int AI_WAREHOUSE_Y = MAP_HEIGHT_PX / 2;
    public static final int PLAYER_WAREHOUSE_X = MAP_WIDTH_PX - 40;
    public static final int PLAYER_WAREHOUSE_Y = MAP_HEIGHT_PX / 2;

    private Cell[][] cells;
    private TextureRegion grassTexture;
    private TextureRegion resourceTexture;
    private TextureRegion warehouseTexture;

    public BattleMap() {
        grassTexture = Assets.getOurInstance().getTextureAtlas().findRegion("grass");
        resourceTexture = Assets.getOurInstance().getTextureAtlas().findRegion("trophy");
        warehouseTexture = Assets.getOurInstance().getTextureAtlas().findRegion("shortButton");
        cells = new Cell[COLUMN_COUNT][ROWS_COUNT];
        for (int x = 0; x < COLUMN_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    public static int getColumnCount() {
        return COLUMN_COUNT;
    }

    public static int getRowsCount() {
        return ROWS_COUNT;
    }

    public static int getCellSize() {
        return CELL_SIZE;
    }

    public int getResourceCount(Vector2 point) {
        int cx = (int) (point.x / CELL_SIZE);
        int cy = (int) (point.y / CELL_SIZE);
        return cells[cx][cy].resource;
    }

    public int harvesterResource(Vector2 point, int power) {
        int cx = (int) (point.x / CELL_SIZE);
        int cy = (int) (point.y / CELL_SIZE);
        int value = 0;
        if (cells[cx][cy].resource >= power) {
            value = power;
            cells[cx][cy].resource -= power;
        } else {
            value = cells[cx][cy].resource;
            cells[cx][cy].resource = 0;
        }
        return value;
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < COLUMN_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                batch.draw(grassTexture, x * CELL_SIZE, y * CELL_SIZE);
                cells[x][y].render(batch);
            }
        }
        batch.setColor(0.7f, 0.1f, 0.0f, 1.0f);
        batch.draw(warehouseTexture,AI_WAREHOUSE_X - 40, AI_WAREHOUSE_Y - 40);
        batch.setColor(0.1f, 0.7f, 0.0f, 1.0f);
        batch.draw(warehouseTexture, PLAYER_WAREHOUSE_X - 40, PLAYER_WAREHOUSE_Y - 40);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {
        for (int x = 0; x < COLUMN_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                cells[x][y].update(dt);
            }
        }
    }
}
