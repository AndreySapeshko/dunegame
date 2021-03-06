package ru.sapeshkoas.dunegame.core.users_logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.sapeshkoas.dunegame.core.GameController;
import ru.sapeshkoas.dunegame.core.units.AbstractUnit;
import ru.sapeshkoas.dunegame.core.units.types.Owner;
import ru.sapeshkoas.dunegame.core.units.types.UnitType;

public class PlayerLogic extends BaseLogic {

    public PlayerLogic(GameController gc) {
        super(gc);
        this.money = 1000;
        this.unitsCount = 10;
        this.unitsMaxCount = 100;
        this.owner = Owner.PLAYER;
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
