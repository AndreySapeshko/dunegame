package ru.sapeshkoas.dunegame.core.interfaces;

import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.units.types.TargetType;

public interface Targetable {
    Vector2 getPosition();
    TargetType getTargetType();
}
