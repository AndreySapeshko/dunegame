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

    public Vector2 getPosition() {
        return position;
    }
}
