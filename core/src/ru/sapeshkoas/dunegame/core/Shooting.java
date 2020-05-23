package ru.sapeshkoas.dunegame.core;

public interface Shooting {

    void shot(Projectile projectile);

    void resultOfShot(Projectile projectile, float dt);
}
