package ru.sapeshkoas.dunegame.core.users_logic;

import ru.sapeshkoas.dunegame.core.GameController;
import ru.sapeshkoas.dunegame.core.units.types.Owner;

public class BaseLogic {
    protected GameController gc;
    protected Owner owner;
    protected int money;
    protected int unitsCount;
    protected int unitsMaxCount;

    public int getMoney() {
        return money;
    }

    public int getUnitsCount() {
        return unitsCount;
    }

    public int getUnitsMaxCount() {
        return unitsMaxCount;
    }

    public Owner getOwner() {
        return owner;
    }

    public BaseLogic(GameController gc) {
        this.gc = gc;
    }

    public void addMoney(int amount) {
        money += amount;
    }
}
