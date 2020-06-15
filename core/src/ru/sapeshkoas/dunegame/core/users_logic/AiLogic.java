package ru.sapeshkoas.dunegame.core.users_logic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.BattleMap;
import ru.sapeshkoas.dunegame.core.GameController;
import ru.sapeshkoas.dunegame.core.units.AbstractUnit;
import ru.sapeshkoas.dunegame.core.units.BattleTank;
import ru.sapeshkoas.dunegame.core.units.Harvester;
import ru.sapeshkoas.dunegame.core.units.types.Owner;
import ru.sapeshkoas.dunegame.core.units.types.UnitType;

import java.util.ArrayList;
import java.util.List;

public class AiLogic extends BaseLogic {
    private float timer;
    private List<BattleTank> tmpAiBattleTanks;
    private List<Harvester> tmpAiHarvester;
    private List<BattleTank> tmpPlayerBattleTanks;
    private List<Harvester> tmpPlayerHarvesters;
    private Vector2 tmp;

    public AiLogic(GameController gc) {
        super(gc);
        this.owner = Owner.AI;
        this.money = 1000;
        this.unitsCount = 10;
        this.unitsMaxCount = 100;
        this.timer = 100;
        this.tmpAiBattleTanks = new ArrayList<>();
        this.tmpPlayerBattleTanks = new ArrayList<>();
        this.tmpPlayerHarvesters = new ArrayList<>();
        this.tmpAiHarvester = new ArrayList<>();
        this.tmp = null;
    }

    public void update(float dt) {
        timer += dt;
        if (timer > 2.0f) {
            timer = 0.0f;
            gc.getUnitsController().collectTanks(tmpAiBattleTanks, gc.getUnitsController().getAiUnits(), UnitType.BATTLE_TANK);
            gc.getUnitsController().collectTanks(tmpPlayerBattleTanks, gc.getUnitsController().getPlayerUnits(), UnitType.BATTLE_TANK);
            gc.getUnitsController().collectTanks(tmpPlayerHarvesters, gc.getUnitsController().getPlayerUnits(), UnitType.HARVESTER);
            gc.getUnitsController().collectTanks(tmpAiHarvester, gc.getUnitsController().getAiUnits(), UnitType.HARVESTER);
            for (int i = 0; i < tmpAiBattleTanks.size(); i++) {
                BattleTank aiBattleTank = tmpAiBattleTanks.get(i);
                if (tmpPlayerBattleTanks.size() > 0) {
                    aiBattleTank.commandAttack(findNearestTarget(aiBattleTank, tmpPlayerBattleTanks));
                }
            }
            for (int i = 0; i < tmpAiHarvester.size(); i++) {
                Harvester aiHarvester = tmpAiHarvester.get(i);
                aiHarvesterProcessing(aiHarvester);
            }
        }
    }

    public <T extends AbstractUnit> T findNearestTarget(AbstractUnit currentUnit, List<T> possibleTargets) {
        T target = null;
        float minDistance = 10000000.0f;
        for (int i = 0; i < possibleTargets.size(); i++) {
            T possibleTarget = possibleTargets.get(i);
            float currentDistance = possibleTarget.getPosition().dst(currentUnit.getPosition());
            if (currentDistance < minDistance) {
                target = possibleTarget;
                minDistance = currentDistance;
            }
        }
        return target;
    }

    public void aiBattleTankProcessing(AbstractUnit aiUnit) {
        if (aiUnit.getTarget() == null) {
            AbstractUnit playerUnit = gc.getUnitsController().getNearestPlayerUnit(aiUnit.getPosition());
            if (playerUnit != null) {
                aiUnit.commandAttack(playerUnit);
                return;
            }
            if (aiUnit.getPosition().dst(aiUnit.getDestination()) < 5) {
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
//            if (aiUnit.getPosition().dst(aiUnit.getDestination()) < 5) {
//                float[][] resourceMap = gc.getBattleMap().getResourceMap();
//                float maxResource = 0.0f;
//                for (int i = 0; i < resourceMap.length; i++) {
//                    for (int j = 0; j < resourceMap[i].length; j++) {
//                        if (resourceMap[i][j] > maxResource) {
//                            aiUnit.getTmp().set(i * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE * 2,
//                                    j * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE * 2);
//                            if (isFreeTarget(aiUnit.getTmp())) {
//                                aiUnit.commandMoveTo(aiUnit.getTmp());
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    public boolean isFreeTarget(Vector2 target) {
        for (int i = 0; i < tmpAiHarvester.size(); i++) {
            Harvester h = tmpAiHarvester.get(i);
            if (target.dst(h.getPosition()) < 200.0f) {
                return false;
            }
        }
        return true;
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
        if (endScanX >= BattleMap.COLUMN_COUNT) {
            endScanX = BattleMap.COLUMN_COUNT - 1;
        }
        if (endScanY >= BattleMap.ROWS_COUNT) {
            endScanY = BattleMap.ROWS_COUNT - 1;
        }
        int resource = 0;
        tmp = null;
        for (int i = cellX; i <= endScanX; i++) {
            for (int j = cellY; j <= endScanY; j++) {
                int cellResource = gc.getBattleMap().getResourceCount(aiUnit.getTmp().set(i * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2,
                        j * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2));
                if (cellResource > resource) {
                    tmp = aiUnit.getTmp();
                    resource = cellResource;
                }
            }
        }
        return tmp;
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
        aiUnit.commandMoveTo(aiUnit.getTmp().set(x, y));
    }

}
