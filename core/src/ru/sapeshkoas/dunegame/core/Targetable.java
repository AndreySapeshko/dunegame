package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.units.TargetType;

public interface Targetable {
    Vector2 getPosition();
    TargetType getTargetType();
}
