package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TanksController extends ObjectPool<Tank> {
    private GameController gc;
    private Vector2 tmp;

    @Override
    protected Tank newObject() {
        return new Tank(gc);
    }

    public TanksController(GameController gc) {
        this.gc = gc;
        this.tmp = new Vector2();
    }

    public void setup(float x, float y, Tank.Owner owner) {
        Tank t = getActiveElement();
        t.setup(x, y, owner);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public Tank getNearsTank(Vector2 point) {
        for (int i = 0; i < activeList.size(); i++) {
            Tank t = activeList.get(i);
            if (t.getOwner() == Tank.Owner.AI && point.dst(t.position) < 30) {
                return t;
            }
        }
            return null;
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        playerUpdate(dt);
        aiUpdate(dt);
        checkPool();
    }

    public void playerUpdate(float dt) {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            tmp.set(Gdx.input.getX(), 720 - Gdx.input.getY());
            for (int i = 0; i < gc.getSelectedUnits().size(); i++) {
                Tank t = gc.getSelectedUnits().get(i);
                if (t.getWeapon().getType() == Weapon.Type.HARVEST) {
                    t.commandMoveTo(tmp);
                }
                if (t.getWeapon().getType() == Weapon.Type.GROUND) {
                    Tank aiTank = getNearsTank(tmp);
                    if (aiTank != null) {
                        t.commandAttack(aiTank);
                    } else {
                        t.commandMoveTo(tmp);
                    }
                }
            }
        }
    }

    public void aiUpdate(float dt) {

    }
}
