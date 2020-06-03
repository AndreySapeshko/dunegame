package ru.sapeshkoas.dunegame.core;

public class Weapon {
    public enum Type {
        GROUND(0), HARVEST(1), AIR(2);
        int imageIndex;

        Type(int imageIndex) {
            this.imageIndex = imageIndex;
        }

        public int getImageIndex() {
            return imageIndex;
        }
    }
    private Type type;
    private float period;
    private float time;
    private int power;
    private float weaponAngle;

    public Weapon(Type type, float period, int power) {
        this.type = type;
        this.period = period;
        this.power = power;
    }

    public Type getType() {
        return type;
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
