package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.screens.utils.Assets;

public class BattleMap {
    private class Cell {
        private int cellX, cellY;
        private Building buildingEntrance;
        private int resource;
        private float resourceRegenerationRate;
        private float resourceRegenerationTime;
        private boolean groundPassable;

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
            groundPassable = true;
        }

        public int getResource() {
            return resource;
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

        public void blockGroundPass() {
            groundPassable = false;
            resource = 0;
            resourceRegenerationRate = 0.0f;
        }

        public void unblockGroundPass() {
            groundPassable = true;
        }
    }
    public static final int COLUMN_COUNT = 20;
    public static final int ROWS_COUNT = 12;
    public static final int CELL_SIZE = 80;
    public static final int MAP_WIDTH_PX = COLUMN_COUNT * CELL_SIZE;
    public static final int MAP_HEIGHT_PX = ROWS_COUNT * CELL_SIZE;

    private float[][] plotProductivity;
    private int cellsInPlot;
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
        cellsInPlot = 4;
        plotProductivity = new float[COLUMN_COUNT / cellsInPlot][ROWS_COUNT / cellsInPlot];
    }

    public boolean isCellGroundPassable(Vector2 position) {
        int cellX = (int) (position.x / CELL_SIZE);
        int cellY = (int) (position.y / CELL_SIZE);
        if (cellX < 0 || cellY < 0 || cellX >= COLUMN_COUNT || cellY >= ROWS_COUNT){
            return false;
        }
        return cells[cellX][cellY].groundPassable;
    }

    public float[][] getPlotProductivity() {
        return plotProductivity;
    }

    public int getCellsInPlot() {
        return cellsInPlot;
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

    public void setupBuildingEntrance(int cellX, int cellY, Building building) {
        cells[cellX][cellY].buildingEntrance = building;
    }

    public Building getBuildingEntrance(int cellX, int cellY) {
        return cells[cellX][cellY].buildingEntrance;
    }

    public void blockGroundCell(int cellX, int cellY) {
        cells[cellX][cellY].blockGroundPass();
    }

    public void unblockGroundCell(int cellX, int cellY) {
        cells[cellX][cellY].unblockGroundPass();
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

    public float[][] getResourceMap() {
        for (int i = 0; i < plotProductivity.length; i++) {
            for (int j = 0; j < plotProductivity[i].length; j++) {
                plotProductivity[i][j] = 0.0f;
                float value = 0.0f;
                for (int x = i * cellsInPlot; x < (i + 1) * cellsInPlot; x++) {
                    for (int y = j * cellsInPlot; y < (j + 1) * cellsInPlot; y++) {
                        value += cells[x][y].resourceRegenerationRate;
                    }
                }
                plotProductivity[i][j] = value;
            }
        }
        return plotProductivity;
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < COLUMN_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                batch.draw(grassTexture, x * CELL_SIZE, y * CELL_SIZE);
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
