package ru.sapeshkoas.dunegame.core;

public class Weapon {
    private float period;
    private float time;
    private int power;
    private float weaponAngle;

    public Weapon(float period, int power) {
        this.period = period;
        this.power = power;
    }

    public float getWeaponAngle() {
        return weaponAngle;
    }

    public void setWeaponAngle(float weaponAngle) {
        this.weaponAngle = weaponAngle;
    }

    public float getUsageTimePercentage() {
        return time / period;
    }

    public void reset() {
        time = 0.0f;
    }

    public int use(float dt) {
        time += dt;
        if (time > period) {
            time = 0.0f;
            return power;
        }
        return -1;
    }
}
