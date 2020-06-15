package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.sapeshkoas.dunegame.core.interfaces.Poolable;
import ru.sapeshkoas.dunegame.core.users_logic.BaseLogic;
import ru.sapeshkoas.dunegame.screens.utils.Assets;

public class Building extends GameObject implements Poolable {
    public enum Type {
        STOCK
    }

    private Type type;
    private TextureRegion texture;
    private Vector2 textureWorldPosition;
    private int cellX, cellY;
    private BaseLogic ownerLogic;
    private int hp;
    private int hpMax;

    public Type getType() {
        return type;
    }

    public BaseLogic getOwnerLogic() {
        return ownerLogic;
    }

    public Building(GameController gc) {
        super(gc);
        this.textureWorldPosition = new Vector2();
    }

    public void setup(int cellX, int cellY, BaseLogic ownerLogic) {
        this.texture = Assets.getOurInstance().getTextureAtlas().findRegion("grass");
        this.type = Type.STOCK;
        this.cellX = cellX;
        this.cellY = cellY;
        this.ownerLogic = ownerLogic;
        this.hpMax = 1000;
        this.hp = hpMax;
        this.position.set(cellX * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2, cellY * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2);
        textureWorldPosition.set((cellX - 1) * BattleMap.CELL_SIZE, cellY * BattleMap.CELL_SIZE);
//        for (int i = cellX - 1; i < cellX + 2; i++) {
//            for (int j = cellY; j < cellY + 2; j++) {
//                gc.getBattleMap().blockGroundCell(i, j);
//            }
//        }
        gc.getBattleMap().setupBuildingEntrance(cellX, cellY - 1, this);
    }

    @Override
    public boolean isActive() {
        return hp > 0;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(0.5f, 0.5f, 0.5f, 0.6f);
        batch.draw(texture,textureWorldPosition.x, textureWorldPosition.y, BattleMap.CELL_SIZE * 3, BattleMap.CELL_SIZE * 2);
        batch.setColor(0.5f, 0.2f, 0.2f, 0.8f);
        batch.draw(texture,textureWorldPosition.x + BattleMap.CELL_SIZE, textureWorldPosition.y - BattleMap.CELL_SIZE, BattleMap.CELL_SIZE, BattleMap.CELL_SIZE);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {

    }

    public void destroy() {
        for (int i = cellX - 1; i < cellX + 1; i++) {
            for (int j = cellY; j < cellY + 1; j++) {
                gc.getBattleMap().unblockGroundCell(i, j);
            }
        }
        gc.getBattleMap().setupBuildingEntrance(cellX, cellY - 1, null);
    }
}
