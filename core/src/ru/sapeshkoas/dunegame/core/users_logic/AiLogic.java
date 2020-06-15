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


}
