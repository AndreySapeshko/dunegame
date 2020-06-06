package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.sapeshkoas.dunegame.core.units.AbstractUnit;
import ru.sapeshkoas.dunegame.core.units.Owner;
import ru.sapeshkoas.dunegame.core.units.UnitType;

public class PlayerLogic {
    private GameController gc;

    public PlayerLogic(GameController gc) {
        this.gc = gc;
    }

    public void update(float dt) {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            for (int i = 0; i < gc.getSelectedUnits().size(); i++) {
                AbstractUnit u = gc.getSelectedUnits().get(i);
                if (u.getOwner() == Owner.PLAYER) {
                    unitsProcessing(u);
                }
            }
        }
    }

    public void unitsProcessing(AbstractUnit unit) {
        if (unit.getUnitType() == UnitType.HARVESTER) {
            unit.commandMoveTo(gc.getMouse());
            return;
        }
        if (unit.getUnitType() == UnitType.BATTLE_TANK) {
            AbstractUnit aiUnit = gc.getUnitsController().getNearestAiUnit(gc.getMouse());
            if (aiUnit != null) {
                unit.commandAttack(aiUnit);
            } else {
                unit.commandMoveTo(gc.getMouse());
            }
        }
    }
}
