package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    protected GameController gc;
    protected Vector2 position;
    protected Vector2 tmp;

    public GameObject(GameController gc) {
        this.gc = gc;
        position = new Vector2();
        tmp = new Vector2();
    }

    public void moveBy(Vector2 toPoint) {
        position.add(toPoint);
    }

    public int getCellX() {
        return (int) (position.x / BattleMap.CELL_SIZE);
    }

    public int getCellY() {
        return (int) (position.y / BattleMap.CELL_SIZE);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getTmp() {
        return tmp;
    }
}
