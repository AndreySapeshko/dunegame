package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.units.AbstractUnit;
import ru.sapeshkoas.dunegame.core.units.UnitType;

import java.util.List;

public class AiLogic {
    private GameController gc;

    public AiLogic(GameController gc) {
        this.gc = gc;
    }

    public void update(float dt) {
        List<AbstractUnit> aiUnits = gc.getUnitsController().getAiUnits();
        for (int i = 0; i < aiUnits.size(); i++) {
            AbstractUnit aiUnit = aiUnits.get(i);
            if (aiUnit.getUnitType() == UnitType.BATTLE_TANK) {
                aiBattleTankProcessing(aiUnit);
            }
            if (aiUnit.getUnitType() == UnitType.HARVESTER) {
                aiHarvesterProcessing(aiUnit);
            }
        }
    }

    public void aiBattleTankProcessing(AbstractUnit aiUnit) {
        if (aiUnit.getTarget() == null) {
            AbstractUnit playerUnit = gc.getUnitsController().getNearestPlayerUnit(aiUnit.getPosition());
            if (playerUnit != null) {
                aiUnit.commandAttack(playerUnit);
                return;
            }
            if (aiUnit.getPosition().dst(aiUnit.getDestination()) < 5) {
                freeMove(aiUnit, 200);
            }
        }
    }

    public void aiHarvesterProcessing(AbstractUnit aiUnit) {
        if (gc.getBattleMap().getResourceCount(aiUnit.getPosition()) < 1) {
            Vector2 target = scanAroundCells(aiUnit, 1);
            if (target != null) {
                aiUnit.commandMoveTo(target);
                return;
            }
            if (aiUnit.getPosition().dst(aiUnit.getDestination()) < 5) {
                freeMove(aiUnit, 100);
            }
        }
    }

    public Vector2 scanAroundCells(AbstractUnit aiUnit, int radius) {
        int cellX = aiUnit.getCellX() - radius;
        int cellY = aiUnit.getCellY() - radius;
        if (cellX < 0) {
            cellX = 0;
        }
        if (cellY < 0) {
            cellY = 0;
        }
        int endScanX = aiUnit.getCellX() + radius;
        int endScanY = aiUnit.getCellY() + radius;
        if (endScanX > 15) {
            endScanX = 15;
        }
        if (endScanY > 8) {
            endScanY = 8;
        }
        for (int i = cellX; i <= endScanX; i++) {
            for (int j = cellY; j <= endScanY; j++) {
                if (gc.getBattleMap().getResourceCount(aiUnit.tmp.set(i * 80 + 40, j * 80 + 40)) > 0) {
                    return aiUnit.tmp;
                }
            }
        }
        return null;
    }

    public void freeMove(AbstractUnit aiUnit, float distance) {
        float k = 1.0f;
        if (MathUtils.random() < 0.5f) {
            k = - 1.0f;
        }
        float x = aiUnit.getPosition().x + k * distance;
        if (MathUtils.random() < 0.5f) {
            k = - 1.0f;
        }
        float y = aiUnit.getPosition().y + k * distance;
        if (x > 1240) {
            x = 1240.0f;
        }
        if (x < 40) {
            x = 40.0f;
        }
        if (y > 680) {
            y = 680.0f;
        }
        if (y < 40) {
            y = 40.0f;
        }
        aiUnit.commandMoveTo(aiUnit.tmp.set(x, y));
    }
}
